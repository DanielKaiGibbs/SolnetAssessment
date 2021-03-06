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

    /**
     * Respond to API requests to fetch data from the database.
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
        String taskData = request.getParameter("jsonData");
        if (taskData == null) {
            out.println("ERROR: 'jsonData' parameter must be provided");
            response.setStatus(400);
            return;
        }

        try {
            JSONObject jsonRequest = new JSONObject(taskData);
            String query = "";

            //Determine the required parameters and ensure they have been presented
            String[] requiredAttributes;
            if (request.getRequestURI().equals("/del"))
                requiredAttributes = new String[]{"id"};
            else if (request.getRequestURI().equals("/add"))
                requiredAttributes = new String[]{"title", "description", "status", "due_date", "creation_date"};
            else if (request.getRequestURI().equals("/update"))
                requiredAttributes = new String[]{"id", "title", "description", "status", "due_date", "creation_date"};
            else {
                response.setStatus(400);
                out.println("Error: Unsupported API request \"" + request.getRequestURI() + "\"");
                return;
            }

            if (!jsonRequest.keySet().containsAll(Arrays.asList(requiredAttributes))) {
                response.setStatus(400);
                out.println("ERROR: Required attributes must be supplied in the json body: " + Arrays.toString(requiredAttributes));
                return;
            }

            //Determine the query to apply to the database
            if (request.getRequestURI().equals("/delete")) {

            }
            //Check that the incoming JSON contains the correct attributes to alter or add a new entry
            else if (request.getRequestURI().equals("/add")) {
                query = "INSERT INTO tasks (title, description, status, due_date, creation_date) " +
                        " VALUES (" +
                        "'" + jsonRequest.get("title") + "', " +
                        "'" + jsonRequest.get("description") + "', " +
                        "'" + jsonRequest.get("status") + "', " +
                        "'" + jsonRequest.get("due_date") + "', " +
                        "'" + jsonRequest.get("creation_date") + "')";
            }
            else if (request.getRequestURI().equals("/update")) {
                query = "UPDATE tasks SET " +
                        "description = '" + jsonRequest.get("description") + "'," +
                        "status = '" + jsonRequest.get("status") + "'," +
                        "due_date = '" + jsonRequest.get("due_date") + "'," +
                        "creation_date = '" + jsonRequest.get("creation_date") + "'" +
                        "WHERE id = '" + jsonRequest.get("id") + "'";
            }

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
