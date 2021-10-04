package it.eng.rspa.cedus.iotmanager.controller;

import it.eng.digitalenabler.keycloak.KeycloakContext;
import it.eng.digitalenabler.keycloak.User;
import it.eng.iot.configuration.Conf;
import it.eng.iot.servlet.AjaxHandler;
import it.eng.tools.LogFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.keycloak.representations.AccessToken;

import java.util.logging.*;

public abstract class CommonController {

	private static final Logger LOGGER = Logger.getLogger(AjaxHandler.class.getName());
	
	static {
		LogFilter logFilter = new LogFilter();
		LOGGER.setFilter(logFilter);
	}

	public static void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();

		if (request.getParameter("lang") != null) {
			session.setAttribute("lang", request.getParameter("lang"));
		} else if (request.getParameter("lang") == null && session.getAttribute("lang") == null) {
			session.setAttribute("lang", Conf.getInstance().getString("default.lang"));
		}
		
		KeycloakContext kc = new KeycloakContext(request);

		// Temporary workaround for SSO logout. Should be fixed in Keycloak 12.0.0
		if(!kc.isValidSession()) {
			session.removeAttribute("token");
			session.removeAttribute("refresh_token");				
			session.removeAttribute("userPerms");
			session.removeAttribute("userIsAdmin");
			session.removeAttribute("isAdmin");
			session.removeAttribute("userInfo");
			session.removeAttribute("username");	
			session.invalidate();
			throw new Exception();
		}			
		
		AccessToken at = kc.getToken();
		Set<String> userRoles = kc.getClientRoles();
		String username = at.getPreferredUsername();
		request.setAttribute("username", username);
		
		@SuppressWarnings("unchecked")
		ArrayList<String> orgs = (ArrayList<String>) at.getOtherClaims().get("organization");
		@SuppressWarnings("unchecked")
		ArrayList<String> group = (ArrayList<String>) at.getOtherClaims().get("groups");
		
		Set<String> orgSet = new HashSet<String>();
		Set<String> groupSet = new HashSet<String>();
		if(orgs != null) {
			for (int i = 0; i < orgs.size(); i++) {
				orgSet.add(orgs.get(i));
			}
		}
		if(group != null) {
			for (int i = 0; i < group.size(); i++) {
				groupSet.add(group.get(i).toLowerCase());
			}
		}

		String token = (String) session.getAttribute("token");
		String refresh_token = (String) session.getAttribute("refresh_token");
		User userInfo = (User) session.getAttribute("userInfo");
		
		if(token == null) {
			token = kc.getTokenString();
			session.setAttribute("token", token);
		}
		if(refresh_token == null) {
			refresh_token = kc.getRefreshToken();
			session.setAttribute("refresh_token", refresh_token);
		}
		if(userInfo == null) {
			userInfo = new User();
			userInfo.setId(at.getSubject());
			userInfo.setUsername(username);
			userInfo.setEmail(at.getEmail());
			userInfo.setRoles(Optional.of(userRoles));
			userInfo.setOrganizations(Optional.of(orgSet));
			userInfo.setGroups(Optional.of(groupSet));
		}
		session.setAttribute("userInfo", userInfo);
		
		try {
			boolean userIsAdmin = false;
			boolean cityEnabled = false;
			boolean facilityEnabled = false;
			boolean farmEnabled = false;
			
			for (String rName : userRoles) {

				if ("admin".equalsIgnoreCase(rName)) {
					userIsAdmin = true;
				}
				if ("city_enabled".equalsIgnoreCase(rName)) {
					cityEnabled = true;
				}
				if ("facility_enabled".equalsIgnoreCase(rName)) {
					facilityEnabled = true;
				}
				if ("farm_enabled".equalsIgnoreCase(rName)) {
					farmEnabled = true;
				}
			}
			
			session.setAttribute("userIsAdmin", userIsAdmin);
			session.setAttribute("cityEnabled", cityEnabled);
			session.setAttribute("facilityEnabled", facilityEnabled);
			session.setAttribute("farmEnabled", farmEnabled);
			session.setAttribute("isTenantEnabled", Conf.getInstance().getString("tenant.enabled"));
			
			request.setAttribute("userId", at.getId());
			LOGGER.log(Level.INFO, "Connected userId: " + at.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.log(Level.SEVERE, "Invalid permission");
			response.setStatus(500);
		}
	}

}
