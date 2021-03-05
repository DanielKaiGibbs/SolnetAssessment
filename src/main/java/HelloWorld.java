import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nz.co.solnet.helper.DatabaseHelper;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

// Extend HttpServlet class
public class HelloWorld extends HttpServlet {

    private String message;

    public void init() throws ServletException {
        // Do required initialization
        message = "Hello World";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response content type
        response.setContentType("text/html");

        // Actual logic goes here.
        PrintWriter out = response.getWriter();

        ResultSet queryResult = DatabaseHelper.getAllTasks();

        try {
            int columnCount = queryResult.getMetaData().getColumnCount();


            while(queryResult.next()) {
                StringBuilder rowString = new StringBuilder();

                System.out.println("COLS: " + columnCount);

                for (int i = 0; i < columnCount;) {
                    rowString.append(queryResult.getString(i + 1));
                    if (++i < columnCount) rowString.append(",");
                }

                System.out.println("<h1>" + rowString.toString() + "</h1>");
                out.println("<h1>" + rowString.toString() + "</h1>");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void destroy() {
        // do nothing.
    }
}
