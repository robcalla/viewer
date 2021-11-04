package it.eng.iot.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.eng.iot.configuration.Conf;

/**
 * Servlet implementation class ImagesHandler
 */
@WebServlet("/ImagesHandler")
public class ImagesHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String destinationScope = Conf.getInstance().getString("ImageUpload.destinationScope");
	private static final String filePath = Conf.getInstance().getString("ImageUpload.path");

	public static enum handlerAction {
		context_images("context_images");

		private final String text;

		private handlerAction(final String text) {
			this.text = text;
		}

		public String toString() {
			return this.text;
		}

	};

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImagesHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String context = ((HttpServletRequest) request).getServletPath().substring(1);
		String fileName = ((HttpServletRequest) request).getPathInfo().substring(1);

		if (fileName.matches("^.*\\.(jpeg|jpg|png|bmp|tiff)$")) {
			String[] splitted = fileName.split("\\.");
			String extension = splitted[splitted.length - 1];
			// fileName = fileName.substring(0,fileName.indexOf("."+extension));

			File file;
			if ("war".equals(destinationScope))
				file = new File(getServletContext().getRealPath(filePath) + fileName);
			else
				file = new File(filePath + fileName);

			handlerAction eAction = handlerAction.valueOf(context);

			switch (eAction) {
			case context_images:
				try {
					byte[] imageData = Files.readAllBytes(file.toPath());
					response.setContentType("image/" + extension);
					response.getOutputStream().write(imageData);
				} catch (IOException e) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
				break;

			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				break;
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		throw new ServletException("GET method required.");
	}

}
