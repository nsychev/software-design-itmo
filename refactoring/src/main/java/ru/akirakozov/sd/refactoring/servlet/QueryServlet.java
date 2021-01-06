package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.storage.Database;
import ru.akirakozov.sd.refactoring.storage.SqliteCursor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        Database database = new Database("test.db");

        if ("max".equals(command)) {
            try (SqliteCursor cursor = database.getCursor("SELECT * FROM product ORDER BY price DESC LIMIT 1")) {
                ResultSet rs = cursor.executeQuery();
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Product with max price: </h1>");

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
        } else if ("min".equals(command)) {
            try (SqliteCursor cursor = database.getCursor("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1")) {
                ResultSet rs = cursor.executeQuery();
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Product with min price: </h1>");

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
        } else if ("sum".equals(command)) {
            try (SqliteCursor cursor = database.getCursor("SELECT SUM(price) FROM PRODUCT")) {
                ResultSet rs = cursor.executeQuery();
                response.getWriter().println("<html><body>");
                response.getWriter().println("Summary price: ");

                if (rs.next()) {
                    response.getWriter().println(rs.getInt(1));
                }
                response.getWriter().println("</body></html>");

                rs.close();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try (SqliteCursor cursor = database.getCursor("SELECT COUNT(*) FROM PRODUCT")) {
                ResultSet rs = cursor.executeQuery();
                response.getWriter().println("<html><body>");
                response.getWriter().println("Number of products: ");

                if (rs.next()) {
                    response.getWriter().println(rs.getInt(1));
                }
                response.getWriter().println("</body></html>");

                rs.close();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
