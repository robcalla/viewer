package it.eng.iot.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import it.eng.iot.configuration.FrontendConf;
import it.eng.iot.utils.RestUtils;

@WebServlet("/grafana")
public class GrafanaHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String grafanaEndpoint = FrontendConf.getInstance().getString("frontend.Grafana.protocol") + "://"
				+ FrontendConf.getInstance().getString("frontend.Grafana.host") + ":" + FrontendConf.getInstance().getString("frontend.Grafana.port")
				+ "/api/search?type=dash-db";

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + FrontendConf.getInstance().getString("frontend.Grafana.apiKey"));

		String result = "";
		PrintWriter out = response.getWriter();
		JSONObject jsonObj;
		JSONArray json;

		try {
			result = RestUtils.consumeGet(grafanaEndpoint, headers);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObj = new JSONObject();
			jsonObj.put("message", "Exception");
			out.write(jsonObj.toString());
		}

		json = new JSONArray(result);

		out.write(json.toString());

	}

}
