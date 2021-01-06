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
public class GetProductsServlet extends HttpServlet {
    private final ProductDAO productDAO;

    public GetProductsServlet(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.getWriter().println("<html><body>");

            for (Product product: productDAO.fetchAll()) {
                response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
            }

            response.getWriter().println("</body></html>");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
