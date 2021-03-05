import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nz.co.solnet.helper.DatabaseHelper;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

// Extend HttpServlet class
public class GetTasksServlet extends HttpServlet {

    private String message;

    public void init() throws ServletException {
        // Do required initialization
        message = "Hello World";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        System.out.println("THING: " + request.getParameter("taskName"));

        ResultSet queryResult = DatabaseHelper.getAllTasks(request.getParameter("taskName"));
        try {
            int columnCount = queryResult.getMetaData().getColumnCount();

            while(queryResult.next()) {
                StringBuilder rowString = new StringBuilder();
                for (int i = 0; i < columnCount;) {
                    rowString.append(queryResult.getString(i + 1));
                    if (++i < columnCount) rowString.append(", ");
                }
                out.println("<p>" + rowString.toString() + "</p>");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void destroy() {
        // do nothing.
    }
}
