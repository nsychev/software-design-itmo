package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.common.CommonServletTest;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddProductServletTest extends CommonServletTest {
    @Test
    void shouldCreateProduct() throws IOException, SQLException {
        AddProductServlet servlet = new AddProductServlet(getProductDAO());

        when(getRequest().getParameter("name")).thenReturn("bread");
        when(getRequest().getParameter("price")).thenReturn("123");

        servlet.doGet(getRequest(), getResponse());

        verify(getProductDAO()).insert("bread", 123);
        assertEquals("OK\n", getResponseText());
    }
}
