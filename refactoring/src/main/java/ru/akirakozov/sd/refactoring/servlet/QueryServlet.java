package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDAO;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final ProductDAO productDAO;

    public QueryServlet(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            try {
                Product product = productDAO.fetchMostExpensive();

                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Product with max price: </h1>");

                if (product != null) {
                    response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
                }
                response.getWriter().println("</body></html>");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try {
                Product product = productDAO.fetchCheapest();

                if (product != null) {
                    response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
                }
                response.getWriter().println("</body></html>");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try {
                response.getWriter().println("<html><body>");
                response.getWriter().println("Summary price: ");
                response.getWriter().println(productDAO.sumPrice());
                response.getWriter().println("</body></html>");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                response.getWriter().println("<html><body>");
                response.getWriter().println("Number of products: ");
                response.getWriter().println(productDAO.count());
                response.getWriter().println("</body></html>");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
