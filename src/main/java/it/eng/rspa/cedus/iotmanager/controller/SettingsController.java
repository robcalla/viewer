package it.eng.rspa.cedus.iotmanager.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.eng.iot.configuration.Conf;

/**
 * Servlet implementation class Settings
 */
@WebServlet("/settings")
public class SettingsController extends HttpServlet {

	private static final long serialVersionUID = 4069678995608104677L;

	public SettingsController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("lang") != null) {
			request.getSession().setAttribute("lang", request.getParameter("lang"));
		} else if (request.getParameter("lang") == null && request.getSession().getAttribute("lang") == null) {
			request.getSession().setAttribute("lang", Conf.getInstance().getString("default.lang"));
		}

		String nextJSP = "/view/settings.jsp";
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
		dispatcher.forward(request, response);

		// TEST
		request.getSession().removeAttribute("token");
		request.getSession().removeAttribute("refresh_token");
	}

}
