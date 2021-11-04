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
import it.eng.digitalenabler.fe.permission.ResourcePermission;
import it.eng.digitalenabler.keycloak.User;
import it.eng.iot.configuration.Conf;
import it.eng.iot.configuration.FrontendConf;
import it.eng.iot.servlet.AjaxHandler;
import it.eng.iot.utils.IdentityManagerUtility;
import it.eng.tools.LogFilter;

import java.util.logging.*;

/**
 * Servlet implementation class IndexController
 */
@WebServlet("/index")
public class IndexController extends HttpServlet {

	private static final long serialVersionUID = 7591147451345558646L;
	private static final Logger LOGGER = Logger.getLogger(AjaxHandler.class.getName());
	private static final Boolean isMultiEnablerOn = Boolean
			.parseBoolean(Conf.getInstance().getString("MultiEnabler.enabled"));
	private static final Boolean isKnowageOn = Boolean
			.parseBoolean(FrontendConf.getInstance().getString("frontend.Knowage.enabled"));
	private static final Boolean isGrafanaOn = Boolean
			.parseBoolean(FrontendConf.getInstance().getString("frontend.Grafana.enabled"));
	private static final Boolean isSupersetOn = Boolean
			.parseBoolean(FrontendConf.getInstance().getString("frontend.Superset.enabled"));

	static {
		LogFilter logFilter = new LogFilter();
		LOGGER.setFilter(logFilter);
	}

	public IndexController() {
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

		String enabler;

		HttpSession session = request.getSession(false);

		if (!isMultiEnablerOn) {
			enabler = Conf.getInstance().getString("MultiEnabler.default");
		} else {
			String enablerFromSession = (String) session.getAttribute("enabler");
			String enablerFromParameter = request.getParameter("enabler");
			enabler = (enablerFromParameter != null) ? enablerFromParameter : enablerFromSession;
		}
		session.setAttribute("enabler", enabler);
		session.setAttribute("isMultiEnablerOn", isMultiEnablerOn);
		session.setAttribute("isKnowageOn", isKnowageOn);
		session.setAttribute("isGrafanaOn", isGrafanaOn);
		session.setAttribute("isSupersetOn", isSupersetOn);

		if (isMultiEnablerOn && ((enabler == null || enabler.trim().isEmpty()))) {
			String nextJSP = "home";
			response.sendRedirect(nextJSP);
		} else {

			Boolean isAdmin = (boolean) session.getAttribute("userIsAdmin");
			User userInfo = (User) session.getAttribute("userInfo");
			if (!isAdmin) {
				ResourcePermission contextPerms = IdentityManagerUtility.getAssetPermission(userInfo,
						ResourceEnum.CONTEXT);
				request.setAttribute("contextPerms", contextPerms);
			} else {
				LOGGER.log(Level.INFO, "User Role: Admin");
			}

			String nextJSP = "/WEB-INF/view/index.jsp";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request, response);
		}

	}

}
