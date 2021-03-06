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

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Set up response writer
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String query = "";

        //Determine the query to apply to the database
        if (request.getRequestURI().equals("/fetchAll")) query = "SELECT * FROM tasks";
        else if (request.getRequestURI().equals("/fetchTask")) {
            query = "SELECT * FROM tasks";

            //Extract the taskName parameter if it is present to filter down to the specified task
            String taskName = request.getParameter("taskName");
            if (taskName != null) query += " WHERE title = '" + taskName + "'";
        }
        else if (request.getRequestURI().equals("/fetchOverdue")) {
            query = "SELECT * FROM tasks WHERE due_date < CAST('" + new java.sql.Date(System.currentTimeMillis()) + "' AS DATE)";
        }

        try {
            //Query the database
            ResultSet queryResult = DatabaseHelper.queryDatabase(query);

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

    public String taskToJSON(ResultSet taskSet) throws SQLException {
        StringBuilder taskJSON = new StringBuilder();
        taskJSON.append("{");
        taskJSON.append("\"id\" : " + taskSet.getString("id") + ", ");
        taskJSON.append("\"title\" : \"" + taskSet.getString("title") + "\", ");
        taskJSON.append("\"description\" : \"" + taskSet.getString("description") + "\", ");
        taskJSON.append("\"status\" : \"" + taskSet.getString("status") + "\", ");
        taskJSON.append("\"due_date\" : \"" + taskSet.getString("due_date") + "\", ");
        taskJSON.append("\"creation_date\" : \"" + taskSet.getString("creation_date") + "\"}");

        return taskJSON.toString();
    }

}
