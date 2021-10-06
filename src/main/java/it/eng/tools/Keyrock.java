package it.eng.tools;

import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;

import it.eng.idmPermission.action.model.Auth;
import it.eng.idmPermission.action.model.Domain;
import it.eng.idmPermission.action.model.Identity;
import it.eng.idmPermission.action.model.Password;
import it.eng.idmPermission.action.model.User;
import it.eng.idmPermission.action.model.UserTokenBean;
import it.eng.iot.configuration.ConfIDM;
import it.eng.iot.servlet.model.OrganizationRole;
import it.eng.iot.utils.RestUtils;
import it.eng.tools.base.IdentityManager;
import it.eng.tools.model.KeyrockToken;
import it.eng.tools.model.Resources;
import java.util.logging.*;


public class Keyrock extends IdentityManager {

	private static final Logger LOGGER = Logger.getLogger(Keyrock.class.getName() );
	
	private String path_user = "/user";
	private String path_token = ConfIDM.getInstance().getString("idm.oauth.token");
	
	public Keyrock() throws Exception {
		super(ConfIDM.getInstance().getString("idm.be.host"));
	}

	@Override
	public String getUserInfo(String token) throws Exception {
		String url = getBaseUrl() + path_user + "?access_token="+token;
		String responseData = RestUtils.consumeGet(url);
		return responseData;
	}
	
	public String refresh_token(String refresh_token, String client_id, String client_secret) 
			throws Exception{
		String url = getBaseUrl() + path_token;
		
		String contentType = MediaType.APPLICATION_FORM_URLENCODED;
		String Authorization = "Basic "
				+ new String(Base64.getEncoder().encode((client_id + ":" + client_secret).getBytes()));
		
		Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", contentType);
			headers.put("Authorization", Authorization);
		
		String reqData = "grant_type=refresh_token"
						+ "&client_id=" + client_id 
						+ "&client_secret=" + client_secret
						+ "&refresh_token=" + refresh_token;
		
		String responseData = RestUtils.consumePost(url, reqData, headers);
		
		return responseData;
		
	}
	
	public KeyrockToken getToken(String code, String client_id, String client_secret, String redirectUri) 
			throws Exception{
		
		String url = getBaseUrl() + path_token;
		
		String auth = "Basic " + new String(Base64.getEncoder()
				.encode((client_id + ":" + client_secret).getBytes()));
		
		Map<String, String> headers = new HashMap<String, String>();
							headers.put("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);
							headers.put("Authorization", auth);
							
		String reqData = "grant_type=authorization_code"
							+ "&code=" + code 
							+ "&redirect_uri=" + redirectUri;
		
		String responseData = RestUtils.consumePost(url, reqData, headers);
		KeyrockToken out = new Gson().fromJson(responseData, KeyrockToken.class);
		
		return out;
	}
	
	
	public static String getAdminToken() throws Exception{
		  
		  Domain domain = new Domain("default");
		  User user = new User("idm", domain, "idm");
		  
		  Password password = new Password(user);
		  Set<String> methods = new HashSet<String>();
		     methods.add("password");
		  
		  Identity identity = new Identity(methods, password);
		  Auth auth = new Auth(identity);
		  
		  UserTokenBean utb = new UserTokenBean(auth);
		  Gson gson = new Gson();
		  String jsutb = gson.toJson(utb);
		  
		  HashMap<String, String> headers = new HashMap<String, String>();
		  String adminToken="";
		  
		  //String idmEndpoint = "http://192.168.150.42:5000/v3/auth/tokens";
		  //String idmEndpoint = "http://cityenabler.eng.it:5000/v3/auth/tokens";
		  String idmEndpoint = ConfIDM.getInstance().getString("idm.keystone.host") + ":" + ConfIDM.getInstance().getString("idm.keystone.port") + ConfIDM.getInstance().getString("idm.v3.auth.token");
		  
		  ClientResponse resp = RestUtils.consumePostFull(idmEndpoint, jsutb, headers);
		  
		  if(resp.getStatus()!=200 && resp.getStatus()!=201 && resp.getStatus()!=301){
		   throw new Exception(String.valueOf(resp.getStatus()));
		  }
		  MultivaluedMap<String,String> respHeaders = resp.getHeaders();
		  adminToken=respHeaders.getFirst("X-Subject-Token");
		   
		  return adminToken;
		 }
	
	
	/** 
	 * Get User Organization Role 
	 * return if member or owner 
	 */
	public OrganizationRole getUserOrganizationRole(String organizationId, String userId) 
			throws Exception{
		
		String endpoint = ConfIDM.getInstance().getString("idm.keystone.host") + ":" + ConfIDM.getInstance().getString("idm.keystone.port") + ConfIDM.getInstance().getString("idm.v3.projects") + organizationId + "/users/"+ userId + "/roles";
		String adminToken = getAdminToken();
		
		Map<String, String> headers = new HashMap<String, String>();
							headers.put("Content-Type", MediaType.APPLICATION_JSON);
							headers.put("X-Auth-Token", adminToken);
							
		String responseData = RestUtils.consumeGet(endpoint, headers);
		OrganizationRole out = new Gson().fromJson(responseData, OrganizationRole.class);
		
		return out;
	}
	
	
	/** Get All Active Organization */
	public Set<Resources> getAllActiveOrganizations() throws Exception{
		String endpoint = ConfIDM.getInstance().getString("idm.keystone.host") + ":" + ConfIDM.getInstance().getString("idm.keystone.port") + ConfIDM.getInstance().getString("idm.v3.organizations") ;
		String adminToken = getAdminToken();
		LOGGER.log(Level.INFO, "adminToken " + adminToken);
		
		Map<String, String> headers = new HashMap<String, String>();
							headers.put("Content-Type", MediaType.APPLICATION_JSON);
							headers.put("X-Auth-Token", adminToken);
							
		String responseData = RestUtils.consumeGet(endpoint, headers);
		LOGGER.log(Level.INFO, responseData);
		JSONObject job = new JSONObject(responseData);
		
		JSONArray resources = new JSONArray(job.get("Resources").toString());
		
		Set<Resources> organizations = new HashSet<>();
		
		for (int i=1; i < resources.length();  i++){
			Resources org = new Gson().fromJson(resources.get(i).toString(), Resources.class);
			if (org.getActive()){
				organizations.add(org);
			}
		}
		return organizations;
	}
	
	
	/** Get All Active Users */
	public Set<Resources> getOrganizationActiveUsers(String organizationId) throws Exception{
		String endpoint = ConfIDM.getInstance().getString("idm.keystone.host") + ":" + ConfIDM.getInstance().getString("idm.keystone.port") + ConfIDM.getInstance().getString("idm.v3.users") ;
		String adminToken = getAdminToken();
		LOGGER.log(Level.INFO, "adminToken " + adminToken);
		
		Map<String, String> headers = new HashMap<String, String>();
							headers.put("Content-Type", MediaType.APPLICATION_JSON);
							headers.put("X-Auth-Token", adminToken);
							
		String responseData = RestUtils.consumeGet(endpoint, headers);
		LOGGER.log(Level.INFO, responseData);
		JSONObject job = new JSONObject(responseData);
		
		JSONArray resources = new JSONArray(job.get("Resources").toString());
		LOGGER.log(Level.INFO, "Resources>> " + resources); 
		
		Set<Resources> users = new HashSet<>();
		
		for (int i=1; i < resources.length();  i++){
			
			String resource = resources.get(i).toString();
			
			JSONObject resourcej = new JSONObject(resource);
			JSONObject urnj = new JSONObject(resourcej.get("urn:scim:schemas:extension:keystone:2.0").toString());
			resourcej.remove("urn:scim:schemas:extension:keystone:2.0");
			resourcej.put("urn", urnj);
															
			Resources user = new Gson().fromJson(resourcej.toString(), Resources.class);
			if (user.getActive() && user.getUrn().getDefault_project_id()!= null){
				if (user.getUrn().getDefault_project_id().equalsIgnoreCase(organizationId)){
					LOGGER.log(Level.INFO, "Organization id: " + user.getUrn().getDefault_project_id()+ " user id: " + user.getId() );
					users.add(user);
				}		
			}
		}
		LOGGER.log(Level.INFO, "Number of users part of the organization: " + users.size()); 
		return users;
	}
	
	
	
	
	

}
