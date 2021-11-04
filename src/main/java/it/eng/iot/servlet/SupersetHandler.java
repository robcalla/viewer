package it.eng.iot.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import it.eng.iot.configuration.FrontendConf;
import it.eng.iot.utils.RestUtils;

@WebServlet("/superset")
public class SupersetHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String supersetEndpoint = FrontendConf.getInstance().getString("frontend.Superset.protocol") + "://"
			+ FrontendConf.getInstance().getString("frontend.Superset.host") + ":" + FrontendConf.getInstance().getString("frontend.Superset.port");

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		String supersetAccessToken = (String) session.getAttribute("superset_access_token");
		String supersetRefreshToken = (String) session.getAttribute("superset_refresh_token");

		boolean isTokenValid = false;

		if (supersetAccessToken != null) {
			isTokenValid = this.isTokenValid(supersetAccessToken);
			if (!isTokenValid) {
				if (supersetRefreshToken != null) {
					supersetAccessToken = this.refreshToken(supersetRefreshToken);
					session.setAttribute("superset_access_token", supersetAccessToken);
				}
			}
		} else {
			this.authenticateAdmin(session);
			supersetAccessToken = (String) session.getAttribute("superset_access_token");
		}

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + supersetAccessToken);

		String result = "";
		PrintWriter out = response.getWriter();
		JSONObject jsonObj;

		String dashUrl = supersetEndpoint + "/api/v1/dashboard/?q=%7B%0A%0A%7D";

		try {
			result = RestUtils.consumeGet(dashUrl, headers);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObj = new JSONObject();
			jsonObj.put("message", "Exception");
			out.write(jsonObj.toString());
		}

		jsonObj = new JSONObject(result);

		out.write(jsonObj.toString());

	}

	private void authenticateAdmin(HttpSession session) {

		String username = FrontendConf.getInstance().getString("frontend.Superset.admin.username");
		String password = FrontendConf.getInstance().getString("frontend.Superset.admin.password");

		JSONObject body = new JSONObject();
		body.put("username", username);
		body.put("password", password);
		body.put("refresh", true);
		body.put("provider", "db");

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		String response = "";
		try {
			response = RestUtils.consumePost(supersetEndpoint + "/api/v1/security/login", body, headers);
		} catch (Exception e) {
			e.getStackTrace();
		}

		JSONObject jsonObj = new JSONObject(response);
		String accessToken = jsonObj.getString("access_token");
		session.setAttribute("superset_access_token", accessToken);
		String refreshToken = jsonObj.getString("refresh_token");
		session.setAttribute("superset_refresh_token", refreshToken);

	}

	private boolean isTokenValid(String accessToken) {

		boolean isValid = false;

		String[] chunks = accessToken.split("\\.");

		String tokenWithoutSignature = chunks[0] + "." + chunks[1];
		String signature = chunks[2];

		SignatureAlgorithm sa = SignatureAlgorithm.HS256;
		SecretKeySpec secretKeySpec = new SecretKeySpec(SignatureAlgorithm.HS256.getValue().getBytes(),
				sa.getJcaName());

		DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);

		if (validator.isValid(tokenWithoutSignature, signature)) {
			isValid = true;
		}

		return isValid;
	}

	private String refreshToken(String refreshToken) {

		String refreshUrl = supersetEndpoint + "/api/v1/security/refresh";
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + refreshToken);

		String response = "";
		try {
			response = RestUtils.consumePost(refreshUrl, new Object(), headers);
		} catch (Exception e) {
			e.getStackTrace();
		}

		JSONObject jsonObj = new JSONObject(response);

		return jsonObj.getString("access_token");

	}

}
