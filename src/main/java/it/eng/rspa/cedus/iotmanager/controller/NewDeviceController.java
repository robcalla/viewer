package it.eng.rspa.cedus.iotmanager.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.*;

/**
 * Servlet implementation class NewDeviceController
 */
@WebServlet("/newdevice")
public class NewDeviceController extends HttpServlet {
	private static final long serialVersionUID = 2536975717238521423L;
	private static final Logger LOGGER = Logger.getLogger(NewDeviceController.class.getName());

	public NewDeviceController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			CommonController.doGet(request, response);
		} catch (Exception e) {

			String scheme = request.getScheme() + "://";
			String serverName = request.getServerName();
			String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
			String contextPath = request.getContextPath();

			String redirectTo = scheme + serverName + serverPort + contextPath;

			LOGGER.log(Level.INFO, "Redirect to " + redirectTo);

			response.sendRedirect(redirectTo);
			return;

		}

		String selectedScope = request.getParameter("scope");
		String selectedUrbanservice = request.getParameter("urbanservice");

		String nextJSP = "/WEB-INF/view/newdevice.jsp";
		if (selectedScope != null && selectedUrbanservice != null) {
			nextJSP = nextJSP + "?scope=" + selectedScope + "&urbanservice=" + selectedUrbanservice;
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);

		dispatcher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("currentDevice", request.getParameter("currentDevice"));
		doGet(request, response);
	}

}
