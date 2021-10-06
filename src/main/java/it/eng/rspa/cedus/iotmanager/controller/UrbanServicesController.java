package it.eng.rspa.cedus.iotmanager.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.eng.digitalenabler.fe.enumeration.ResourceEnum;
import it.eng.digitalenabler.fe.permission.Resource;
import it.eng.digitalenabler.fe.permission.ResourcePermission;
import it.eng.digitalenabler.keycloak.User;
import it.eng.iot.utils.IdentityManagerUtility;

import java.util.logging.*;

/**
 * Servlet implementation class UrbanServicesController
 */
@WebServlet("/urbanservices")
public class UrbanServicesController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(UrbanServicesController.class.getName() );
       
    public UrbanServicesController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try{ CommonController.doGet(request, response); }
		catch(Exception e){		
			String scheme = request.getScheme() + "://";
		    String serverName = request.getServerName();
		    String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
		    String contextPath = request.getContextPath();
						
			String redirectTo = scheme + serverName + serverPort + contextPath;
			
			LOGGER.log(Level.INFO, "Redirect to "+redirectTo);
			
			response.sendRedirect(redirectTo);
			return;
		}
		
		String nextJSP = "home";		
		String selectedScope = request.getParameter("scope");
		if( selectedScope == null || selectedScope.trim().isEmpty()){
			response.sendRedirect(nextJSP);
			return;
		}

		HttpSession session = request.getSession(false);
		Boolean isAdmin = (boolean)session.getAttribute("userIsAdmin");
		User userInfo = (User) session.getAttribute("userInfo");
		
		//TODO Check if the user has access to this area
		
		if(!isAdmin) {
			Resource refersTo = new Resource(ResourceEnum.CONTEXT, selectedScope);
			ResourcePermission categoryPerms = IdentityManagerUtility.getAssetPermission(userInfo, ResourceEnum.CATEGORY, refersTo);
			request.setAttribute("categoryPerms", categoryPerms);
		}
		
		nextJSP = "/WEB-INF/view/urbanservices.jsp";
		nextJSP = nextJSP + "?scope="+selectedScope;
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
		dispatcher.forward(request,response);
		
	}

}
