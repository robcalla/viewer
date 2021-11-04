package it.eng.rspa.cedus.iotmanager.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.eng.iot.configuration.Conf;
import it.eng.iot.servlet.AjaxHandler;
import it.eng.tools.LogFilter;

import java.util.logging.*;

/**
 * Servlet implementation class IndexController
 */
@WebServlet("/home")
public class HomeController extends HttpServlet {

	private static final long serialVersionUID = 7591147451345558646L;
	private static final Logger LOGGER = Logger.getLogger(AjaxHandler.class.getName());
	private static final Boolean isMultiEnablerOn = Boolean
			.parseBoolean(Conf.getInstance().getString("MultiEnabler.enabled"));

	static {
		LogFilter logFilter = new LogFilter();
		LOGGER.setFilter(logFilter);
	}

	public HomeController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String appVersion = getClass().getPackage().getImplementationVersion();
		HttpSession session = request.getSession();

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

		boolean cityEnabled = (boolean) session.getAttribute("cityEnabled");
		boolean facilityEnabled = (boolean) session.getAttribute("facilityEnabled");
		boolean farmEnabled = (boolean) session.getAttribute("farmEnabled");

		String enabler = getUniqueEnabler(cityEnabled, facilityEnabled, farmEnabled);

		request.setAttribute("version", new String(appVersion));

		String nextJSP = "index";
		if (!isMultiEnablerOn) {
			response.sendRedirect(nextJSP);
			return;
		}
		if (!"false".equalsIgnoreCase(enabler)) {
			session.setAttribute("enabler", enabler);
			session.setAttribute("home", "false");
			response.sendRedirect(nextJSP);
			return;
		} else {
			session.setAttribute("home", "true");
		}

		nextJSP = "/WEB-INF/view/home.jsp";
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
		dispatcher.forward(request, response);

	}

	private String getUniqueEnabler(boolean city, boolean facility, boolean farm) {

		if (city == true && facility == false && farm == false) {
			return "city";
		} else if (city == false && facility == true && farm == false) {
			return "facility";
		} else if (city == false && facility == false && farm == true) {
			return "farm";
		}
		return "false";
	}

}
