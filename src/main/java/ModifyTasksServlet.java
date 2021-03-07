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
     * Respond to API requests to modify data from the database.
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

        //Extract the JSON describing the specified task
        String payloadParam = request.getParameter("payload");
        if (payloadParam == null) {
            out.println("ERROR: 'payload' parameter must be provided");
            response.setStatus(400);
            return;
        }

        try {
            JSONObject jsonPayload = new JSONObject(payloadParam);
            String query = "";

            //Ensure that the incoming JSON contains the correct attributes to alter or add a new entry
            String[] requiredAttributes;
            if (request.getRequestURI().equals("/add"))
                requiredAttributes = new String[]{"title", "description", "status", "due_date", "creation_date"};
            else requiredAttributes = new String[]{"id", "title", "description", "status", "due_date", "creation_date"};

            if (!jsonPayload.keySet().containsAll(Arrays.asList(requiredAttributes))) {
                response.setStatus(400);
                out.println("ERROR: Required attributes must be supplied in the json body: " + Arrays.toString(requiredAttributes));
                return;
            }

            //Determine the query to apply to the database
            if (request.getRequestURI().equals("/add")) {
                query = "INSERT INTO tasks (title, description, status, due_date, creation_date) " +
                        " VALUES (" +
                        "'" + jsonPayload.get("title") + "', " +
                        "'" + jsonPayload.get("description") + "', " +
                        "'" + jsonPayload.get("status") + "', " +
                        "'" + jsonPayload.get("due_date") + "', " +
                        "'" + jsonPayload.get("creation_date") + "')";
            }
            else if (request.getRequestURI().equals("/update")) {
                query = "UPDATE tasks SET " +
                        "title = '" + jsonPayload.get("title") + "'," +
                        "description = '" + jsonPayload.get("description") + "'," +
                        "status = '" + jsonPayload.get("status") + "'," +
                        "due_date = '" + jsonPayload.get("due_date") + "'," +
                        "creation_date = '" + jsonPayload.get("creation_date") + "'" +
                        "WHERE id = " + jsonPayload.get("id");
            }

            //Apply the query to the database
            DatabaseHelper.updateDatabase(query);

            out.println("Successfully modified the database");
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
