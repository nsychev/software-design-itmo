package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.common.CommonServletTest;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.model.ProductFaker;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GetProductsServletTest extends CommonServletTest {
    @Test
    void shouldFormatProducts() throws SQLException {
        GetProductsServlet servlet = new GetProductsServlet(getProductDAO());

        List<Product> products = Arrays.asList(
            ProductFaker.getProduct(),
            ProductFaker.getProduct()
        );

        when(getProductDAO().fetchAll()).thenReturn(products);
        servlet.doGet(getRequest(), getResponse());

        assertEquals(
            "<html><body>\n" +
            products.get(0).getName() + "\t" + products.get(0).getPrice() + "</br>\n" +
            products.get(1).getName() + "\t" + products.get(1).getPrice() + "</br>\n" +
            "</body></html>\n",
            getResponseText()
        );
    }
}
