package ru.akirakozov.sd.refactoring.common;

import org.junit.jupiter.api.BeforeEach;
import ru.akirakozov.sd.refactoring.dao.ProductDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class CommonServletTest {
    private ProductDAO productDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    public void prepareMocks() throws IOException {
        productDAO = mock(ProductDAO.class);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);
    }

    protected ProductDAO getProductDAO() {
        return productDAO;
    }

    protected HttpServletRequest getRequest() {
        return request;
    }

    protected HttpServletResponse getResponse() {
        return response;
    }

    protected String getResponseText() {
        printWriter.flush();
        return stringWriter.toString();
    }
}
