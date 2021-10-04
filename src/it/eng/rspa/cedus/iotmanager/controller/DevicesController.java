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

/**
 * Servlet implementation class IndexController
 */
@WebServlet("/dashboard")
public class DevicesController extends HttpServlet {

	private static final long serialVersionUID = 7591147451345558646L;

	public DevicesController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			CommonController.doGet(request, response);
		} catch (Exception e) {			
			e.printStackTrace();
		}

		String selectedScope = request.getParameter("scope");
		String selectedUrbanservice = request.getParameter("urbanservice");

		HttpSession session = request.getSession();
		Boolean isAdmin = (boolean) session.getAttribute("userIsAdmin");

		if (selectedScope == null || selectedScope.trim().isEmpty()) {
			response.sendRedirect("index");
		} else if (selectedUrbanservice == null || selectedUrbanservice.trim().isEmpty()) {
			response.sendRedirect("urbanservices");
		} else {
	
			if (!isAdmin) {
				Resource refersTo = new Resource(ResourceEnum.CONTEXT, selectedScope);
				ResourcePermission dashboardPerms = IdentityManagerUtility
						.getAssetPermission((User) session.getAttribute("userInfo"), ResourceEnum.CATEGORY, refersTo);
				
				if (dashboardPerms.getCanRead()) {
					//TODO Change into CATEGORY(selectedUrbanservice) and apply the corresponding logic
					refersTo = new Resource(ResourceEnum.CONTEXT, selectedScope);
					dashboardPerms = IdentityManagerUtility.getAssetPermission((User) session.getAttribute("userInfo"),
							ResourceEnum.DASHBOARD, refersTo);
				}
				 
				request.setAttribute("dashboardPerms", dashboardPerms);
	
			}
	
			String nextJSP = "/WEB-INF/view/devices.jsp";
			nextJSP = nextJSP + "?scope=" + selectedScope + "&urbanservice=" + selectedUrbanservice;
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request, response);
		}
	}

}
