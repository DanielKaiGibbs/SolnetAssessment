import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nz.co.solnet.helper.DatabaseHelper;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FetchTasksServlet extends HttpServlet {

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
        String query = "";

        //Determine the query to apply to the database
        if (request.getRequestURI().equals("/fetch")) {
            query = "SELECT * FROM tasks";

            //Extract the taskName parameter to filter down to the specified task
            String idParam = request.getParameter("id");
            if (idParam != null) query += " WHERE id = '" + idParam + "'";
        }
        else if (request.getRequestURI().equals("/fetchOverdue")) {
            query = "SELECT * FROM tasks WHERE due_date < CAST('" + new java.sql.Date(System.currentTimeMillis()) + "' AS DATE)";
        }

        try {
            //Query the database
            ResultSet queryResult = DatabaseHelper.queryDatabase(query);

            //Return an empty set if there are no queries returned from the query
            if (!queryResult.next()) {
                out.println("[]");
                response.setStatus(200);
                return;
            }

            StringBuilder jsonOutput = new StringBuilder("[");
            while(true) {
                try {
                    jsonOutput.append(taskToJSON(queryResult));
                } catch (SQLException e) {
                    out.println("Malformed database entries: " + e);
                    response.setStatus(400);
                    return;
                }

                if (queryResult.next()) jsonOutput.append(", ");
                else break;
            }
            jsonOutput.append("]");
            out.println(jsonOutput.toString());
            response.setStatus(200);

        } catch (SQLException e) {
            response.setStatus(400);
            out.println("Error querying the database: " + e);
        }
    }

    public void destroy() {}

    /**
     * Converts a task to a json string.
     * @param task
     * @return
     * @throws SQLException
     */
    public String taskToJSON(ResultSet task) throws SQLException {
        StringBuilder taskJSON = new StringBuilder();

        taskJSON.append("{");
        taskJSON.append("\"id\" : " + task.getString("id") + ", ");
        taskJSON.append("\"title\" : \"" + task.getString("title") + "\", ");
        taskJSON.append("\"description\" : \"" + task.getString("description") + "\", ");
        taskJSON.append("\"status\" : \"" + task.getString("status") + "\", ");
        taskJSON.append("\"due_date\" : \"" + task.getString("due_date") + "\", ");
        taskJSON.append("\"creation_date\" : \"" + task.getString("creation_date") + "\"}");

        return taskJSON.toString();
    }

}
