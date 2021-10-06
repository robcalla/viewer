package it.eng.iot.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import it.eng.iot.utils.PropertyManager;

/**
* Servlet implementation class LoadConfigs
*/
@WebServlet("/LoadConfigs")
public class LoadConfigs extends HttpServlet {
    private static final long serialVersionUID = 1L;

   /**
    * Default constructor.
    */
   public LoadConfigs() {

   }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Map<String, String> prop = null;
        JSONObject json;
        try {
            prop = PropertyManager.getProperties();
        }catch (Exception e) {
            e.printStackTrace();
            json=new JSONObject();
            json.put("message", "Exception");
            out.write(json.toString());
        }

        json=new JSONObject(prop);

        out.write(json.toString());

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
