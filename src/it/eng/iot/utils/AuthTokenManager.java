package it.eng.iot.utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import com.google.gson.Gson;
import it.eng.iot.configuration.Conf;
import it.eng.iot.configuration.ConfIDM;
import it.eng.iot.servlet.model.Organization;
import it.eng.iot.servlet.model.OrganizationInternalRole;
import it.eng.iot.servlet.model.OrganizationRole;
import it.eng.iot.servlet.model.Permission;
import it.eng.iot.servlet.model.RoleBean;
import it.eng.iot.servlet.model.TokenBean;
import it.eng.iot.servlet.model.UserInfoBean;
import it.eng.tools.Keyrock;
import it.eng.tools.LogFilter;
import it.eng.tools.model.KeyrockToken;
import it.eng.tools.model.Resources;
import java.util.logging.*;

public class AuthTokenManager{
	
	private UserInfoBean userinfo;
	private static final Logger LOGGER = Logger.getLogger(AuthTokenManager.class.getName() );
	
	static {
		LogFilter logFilter = new LogFilter();
		LOGGER.setFilter(logFilter);
	}
	
	public boolean checkToken(String token) {
		
		boolean isvalidtoken = true;
		
		try{
			LOGGER.log(Level.INFO, "User Access Token: " + token);
			setUserInfo(getRemoteUserInfo(token));
		}
		catch(Exception e){
			setUserInfo(null);
			isvalidtoken = false;
		}
		
		return isvalidtoken;
		
	}

	public TokenBean refreshToken(String refresh_token) 
			throws ServletException, IOException {

		String client_id = ConfIDM.getInstance().getString("idm.client.id");
		String client_secret = ConfIDM.getInstance().getString("idm.client.secret");

		TokenBean tb = new TokenBean();

		try {
			Keyrock keyrock = new Keyrock();
			String out = keyrock.refresh_token(refresh_token, client_id, client_secret);
			
			KeyrockToken newtoken = new Gson().fromJson(out, KeyrockToken.class);
			
			Integer expires_in =newtoken.getExpires_in();
			Long currentDate = System.currentTimeMillis();
			Long validityDate = Long.sum(currentDate, expires_in);
			
			// Build the object to send to the client
			tb = new TokenBean(newtoken.getAccess_token(), newtoken.getRefresh_token(), "", String.valueOf(validityDate));

		} 
		catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return tb;
	}
	
	public UserInfoBean getRemoteUserInfo(String token)
			throws Exception {
		
		Keyrock keyrock = new Keyrock();

		String j_userinfo = keyrock.getUserInfo(token);
		LOGGER.log(Level.INFO, "/user responded: " + j_userinfo);
		return new Gson().fromJson(j_userinfo, UserInfoBean.class);

	}
	
	public UserInfoBean getUserInfo(){
		return this.userinfo;
	}
	
	public void setUserInfo(UserInfoBean info){
		this.userinfo = info;
	}
	
}
