package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.dao.ProductDAO;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.storage.Database;


/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Database database = new Database("test.db");
        ProductDAO productDAO = new ProductDAO(database);
        AddProductServlet addProductServlet = new AddProductServlet(productDAO);
        GetProductsServlet getProductsServlet = new GetProductsServlet(productDAO);
        QueryServlet queryServlet = new QueryServlet(productDAO);

        database.initialize();

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(addProductServlet), "/add-product");
        context.addServlet(new ServletHolder(getProductsServlet),"/get-products");
        context.addServlet(new ServletHolder(queryServlet),"/query");

        server.start();
        server.join();
    }
}
