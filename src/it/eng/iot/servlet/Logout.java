package it.eng.iot.servlet;

import it.eng.iot.configuration.ConfIDM;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/logout")
public class Logout extends HttpServlet {
	
	private static final long serialVersionUID = 693724303976303888L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nofollow = request.getParameter("nofollow");
		
		HttpSession session = request.getSession();
		session.removeAttribute("token");
		session.removeAttribute("refresh_token");
		
		session.removeAttribute("userInfo");
		session.removeAttribute("userPerms");
		session.removeAttribute("userIsAdmin");
		session.removeAttribute("isAdmin");
		
		session.removeAttribute("isMultiEnablerOn");
		session.removeAttribute("enabler");
		
		session.invalidate();
		
		if (!("true").equalsIgnoreCase(nofollow)) {
			String host = ConfIDM.getInstance().getString("keycloak.host");
			String logoutUrl = ConfIDM.getInstance().getString("keycloak.logout");
			String redirect = ConfIDM.getInstance().getString("keycloak.redirect");
			response.sendRedirect(host + logoutUrl + redirect);
		}
	}

}
