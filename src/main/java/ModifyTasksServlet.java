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

public class ModifyTasksServlet extends HttpServlet {

    public void init() throws ServletException {}

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Set up response writer
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String JSONStringData = request.getParameter("taskData");
        if (JSONStringData == null) {
            out.println("ERROR: 'taskData' parameter must be provided");
            response.setStatus(400);
            return;
        }

        try {
            JSONObject updateRequest = new JSONObject(JSONStringData);

            //Check that the incoming JSON contains the correct attributes
            String[] requiredAttributes = new String[]{"title", "description", "status", "due_date", "creation_date"};
            if (!updateRequest.keySet().containsAll(Arrays.asList(requiredAttributes))) {
                response.setStatus(400);
                out.println("ERROR: Required attributes must be supplied in the json body: " + Arrays.toString(requiredAttributes));
                return;
            }

            String query = "INSERT INTO tasks (title, description, status, due_date, creation_date) " +
                    " VALUES (" +
                    "'" + updateRequest.get("title") + "', " +
                    "'" + updateRequest.get("description") + "', " +
                    "'" + updateRequest.get("status") + "', " +
                    "'" + updateRequest.get("due_date") + "', " +
                    "'" + updateRequest.get("creation_date") + "')";

            System.out.println(query);

            //Apply the query to the database
            DatabaseHelper.updateDatabase(query);

            out.println("Successfully inserted new task into the database");
            response.setStatus(200);
        } catch(JSONException e) {
            response.setStatus(400);
            out.println("Error parsing JSON parameter: " + e);
        } catch (SQLException e) {
            response.setStatus(400);
            out.println("Error inserting JSON into the database: " + e);
        }
    }

    public void destroy() {}
}
