package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.storage.Database;
import ru.akirakozov.sd.refactoring.storage.SqliteCursor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Database database = new Database("test.db");
        try (SqliteCursor cursor = database.getCursor("SELECT * FROM PRODUCT")) {
            response.getWriter().println("<html><body>");

            ResultSet rs = cursor.executeQuery();

            while (rs.next()) {
                String  name = rs.getString("name");
                int price  = rs.getInt("price");
                response.getWriter().println(name + "\t" + price + "</br>");
            }
            response.getWriter().println("</body></html>");

            rs.close();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
