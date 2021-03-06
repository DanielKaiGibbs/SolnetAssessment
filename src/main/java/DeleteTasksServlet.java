import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nz.co.solnet.helper.DatabaseHelper;
import java.io.*;
import java.sql.ResultSet;

import org.json.JSONException;
import org.json.JSONObject;
import java.sql.SQLException;
import java.util.Arrays;

public class DeleteTasksServlet extends HttpServlet {

    public void init() throws ServletException {}

    /**
     * Respond to API requests to delete data from the database.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Set up response writer
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        //Extract the json describing the specified task
        String idParam = request.getParameter("id");
        if (idParam == null) {
            out.println("ERROR: 'id' parameter must be provided");
            response.setStatus(400);
            return;
        }

        try {
            String query = "DELETE FROM tasks WHERE id = " + idParam;

            System.out.println(query);

            //Apply the query to the database
            DatabaseHelper.updateDatabase(query);

            out.println("Successfully deleted data from the database");
            response.setStatus(200);

        } catch (SQLException e) {
            response.setStatus(400);
            out.println("Error deleting from the database: " + e);
        }
    }

    public void destroy() {}
}
