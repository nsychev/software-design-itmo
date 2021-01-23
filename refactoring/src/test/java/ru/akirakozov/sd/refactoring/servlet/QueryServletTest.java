package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.common.CommonServletTest;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.model.ProductFaker;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class QueryServletTest extends CommonServletTest {
    @Test
    void shouldReturnMostExpensiveProduct() throws SQLException, IOException {
        QueryServlet servlet = new QueryServlet(getProductDAO());

        when(getRequest().getParameter("command")).thenReturn("max");

        Product product = ProductFaker.getProduct();
        when(getProductDAO().fetchMostExpensive()).thenReturn(product);

        servlet.doGet(getRequest(), getResponse());

        assertEquals(
            "<html><body>\n" +
            "<h1>Product with max price: </h1>\n" +
            product.getName() + "\t" + product.getPrice() + "</br>\n" +
            "</body></html>\n",
            getResponseText()
        );
    }

    @Test
    void shouldReturnCheapestProduct() throws SQLException, IOException {
        QueryServlet servlet = new QueryServlet(getProductDAO());

        when(getRequest().getParameter("command")).thenReturn("min");

        Product product = ProductFaker.getProduct();
        when(getProductDAO().fetchCheapest()).thenReturn(product);

        servlet.doGet(getRequest(), getResponse());

        assertEquals(
                "<html><body>\n" +
                product.getName() + "\t" + product.getPrice() + "</br>\n" +
                "</body></html>\n",
                getResponseText()
        );
    }

    @Test
    void shouldReturnPriceSum() throws SQLException, IOException {
        QueryServlet servlet = new QueryServlet(getProductDAO());

        when(getRequest().getParameter("command")).thenReturn("sum");

        when(getProductDAO().sumPrice()).thenReturn(123456);

        servlet.doGet(getRequest(), getResponse());

        assertEquals(
                "<html><body>\n" +
                "Summary price: \n" +
                "123456\n" +
                "</body></html>\n",
                getResponseText()
        );
    }

    @Test
    void shouldReturnCount() throws SQLException, IOException {
        QueryServlet servlet = new QueryServlet(getProductDAO());

        when(getRequest().getParameter("command")).thenReturn("count");

        Product product = ProductFaker.getProduct();
        when(getProductDAO().count()).thenReturn(1337);

        servlet.doGet(getRequest(), getResponse());

        assertEquals(
                "<html><body>\n" +
                "Number of products: \n" +
                "1337\n" +
                "</body></html>\n",
                getResponseText()
        );
    }
}
