package it.eng.iot.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import it.eng.iot.configuration.Conf;

@WebServlet("/uploadhandler")
public class UploadHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean isMultipart;
	private int maxFileSize = 7 * 1024 * 1024;
	private int maxMemSize = 14 * 1024 * 1024;
	private File file;
	private static final String destinationScope = Conf.getInstance().getString("ImageUpload.destinationScope");
	private static final String filePath = Conf.getInstance().getString("ImageUpload.path");
	private static final String tempFilePath = Conf.getInstance().getString("ImageUpload.tempPath");

	public UploadHandler() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		throw new ServletException("GET method used with " + getClass().getName() + ": POST method required.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Check that we have a file upload request
		isMultipart = ServletFileUpload.isMultipartContent(request);

		DiskFileItemFactory factory = new DiskFileItemFactory();

		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);

		// Path for saving temp data
		factory.setRepository(new File(getServletContext().getRealPath(tempFilePath)));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);

		try {
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);

			// Process the uploaded file items
			Iterator i = fileItems.iterator();

			Map<String, String> fields = new HashMap<String, String>();
			FileItem toBeUploaded = null;

			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();

				if (fi.isFormField()) {
					String fieldName = fi.getFieldName();
					String fieldValue = fi.getString();

					fields.put(fi.getFieldName(), fi.getString());
				} else {
					toBeUploaded = fi;
				}

			}

			String scope = fields.get("scope");
			String fileExt = fields.get("ext");
			String remoteUrl = fields.get("remoteUrl");
			boolean isDefault = Boolean.parseBoolean(fields.get("default"));

			if (isDefault) {
				String[] extensions = { ".jpg", ".jpeg", ".png" };
				for (String s : extensions) {
					if ("war".equals(destinationScope))
						file = new File(getServletContext().getRealPath(filePath) + scope + s);
					else
						file = new File(filePath + scope + s);
					file.delete();
				}
				response.getWriter().write("Image changed to default");
			}

			if (toBeUploaded != null || remoteUrl != null) {
//				If the upload type is not setted or if it is a local upload
				String isRemoteUpload = fields.get("isRemoteUpload");
				if (isRemoteUpload == null || isRemoteUpload.compareTo("false") == 0) {
					// Get the uploaded file parameters
					String fieldName = toBeUploaded.getFieldName();
					String fileName = toBeUploaded.getName();
					String contentType = toBeUploaded.getContentType();
					boolean isInMemory = toBeUploaded.isInMemory();
					long sizeInBytes = toBeUploaded.getSize();

					// Convert the back-slashes into slashes
					fileName = fileName.replace("\\", "/");
					fileName = (fileName.lastIndexOf("/") >= 0) ? fileName.substring(fileName.lastIndexOf("/"))
							: fileName.substring(fileName.lastIndexOf("/") + 1);

					// File name composing
					String[] fileNameSplitted = fileName.split("\\.");
					fileExt = fileNameSplitted[fileNameSplitted.length - 1];

					fileName = (scope != null) ? scope.concat(".").concat(fileExt) : fileName;
					fileName = fileName.toLowerCase().replace(" ", "_");
					// Write the file
					if ("war".equals(destinationScope))
						file = new File(getServletContext().getRealPath(filePath) + fileName);
					else
						file = new File(filePath + fileName);

					if (file.delete())
						System.out.println(file.getName() + " overwrited!");

					toBeUploaded.write(file);
					System.out.println("File Uploaded at: " + file.getAbsolutePath());
				} else {
					URL url = new URL(remoteUrl);
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
					con.setRequestMethod("HEAD");
					con.connect();
					String contentType = con.getContentType();
					con.disconnect();

					String ext = contentType.substring(contentType.indexOf("/") + 1);
					if (!("jpg".equals(ext) || "jpeg".equals(ext) || "png".equals(ext))) {
						throw new Exception("Unsupported type");
					} else {
						ext = "jpg";
						String fileName = scope.concat(".").concat(ext);
						con = (HttpURLConnection) url.openConnection();
						con.setRequestProperty("User-Agent",
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
						con.setRequestMethod("GET");
						con.connect();
						InputStream in = con.getInputStream();
						Files.copy(in, Paths.get(getServletContext().getRealPath(filePath) + fileName));
						System.out.println(
								"File Uploaded at: " + Paths.get(getServletContext().getRealPath(filePath) + fileName));
					}
				}
			}

			response.getWriter().write("OK");

		} catch (Exception ex) {
			System.out.println(ex);
			response.getWriter().write("ERROR");
		}

	}

}
