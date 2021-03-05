import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nz.co.solnet.helper.DatabaseHelper;
import java.io.*;
import java.sql.ResultSet;
import org.json.JSONObject;
import java.sql.SQLException;

public class ModifyTasksServlet extends HttpServlet {

    public void init() throws ServletException {}

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            JSONObject updateRequest = new JSONObject(request.getParameter("taskData"));
        } catch(org.json.JSONException) {
            response.setStatus(400);

        }
        //Set up response writer
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String query = "";

        //Query the database
        ResultSet queryResult = DatabaseHelper.queryDatabase(query);

        out.println("<p>UPDATE</p>");
        response.setStatus(200);
    }

    public void destroy() {}
}
