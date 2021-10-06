package it.eng.digitalenabler.keycloak;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;

import it.eng.iot.configuration.ConfIDM;
import it.eng.iot.utils.RestUtils;

public class KeycloakContext {
	
	private KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public KeycloakContext(HttpServletRequest req) {
		this.principal = (KeycloakPrincipal)req.getUserPrincipal();
	}
	
	public KeycloakPrincipal<RefreshableKeycloakSecurityContext> getPrincipal() {
		return principal;
	}
	
	public KeycloakContext() {
		this.principal = this.getPrincipal();
	}

	public AccessToken getToken() {
		return this.principal.getKeycloakSecurityContext().getToken();
	}
	
	public String getTokenString() {
		return this.principal.getKeycloakSecurityContext().getTokenString();
	}
	
	public String getServiceAccessToken() {
		
		String endpoint = ConfIDM.getInstance().getString("keycloak.host") + "/realms/" + ConfIDM.getInstance().getString("keycloak.realm") + "/protocol/openid-connect/token";
		
		HttpClient client = new HttpClient();
		PostMethod httppost = new PostMethod(endpoint);
		httppost.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		
		httppost.addParameter(new NameValuePair("client_id", ConfIDM.getInstance().getString("keycloak.tm.clientId")));
		httppost.addParameter(new NameValuePair("client_secret", ConfIDM.getInstance().getString("keycloak.tm.secret")));
		httppost.addParameter(new NameValuePair("grant_type", "client_credentials"));
		
		JSONObject json = new JSONObject();
		try {
			int statusCode = client.executeMethod(httppost);
			if (statusCode == HttpStatus.SC_OK) {
				String response = httppost.getResponseBodyAsString();
				json = new JSONObject(response);
			}		
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return json.getString("access_token");
		
	}
	
	public Set<String> getClientRoles() {
		return this.getToken().getResourceAccess(ConfIDM.getInstance().getString("keycloak.clientId")).getRoles();
	}
	
	public String getRefreshToken() {
		return this.principal.getKeycloakSecurityContext().getRefreshToken();
	}
	
	public boolean isValidSession() {
		String endpoint = ConfIDM.getInstance().getString("keycloak.host") + "/realms/" + ConfIDM.getInstance().getString("keycloak.realm") + "/protocol/openid-connect/userinfo";
		Integer responseCode = 0;
		try {
			responseCode = RestUtils.validateSession(endpoint, this.getTokenString());
		} catch (Exception e) {
			return false;
		}
		if(responseCode == 200) {
			return true;
		} else {
			return false;
		}
	}
	
}
