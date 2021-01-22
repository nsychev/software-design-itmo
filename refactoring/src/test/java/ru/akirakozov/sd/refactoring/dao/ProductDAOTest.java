package ru.akirakozov.sd.refactoring.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.common.CommonDatabaseTest;
import ru.akirakozov.sd.refactoring.common.Faker;
import ru.akirakozov.sd.refactoring.common.SimpleProduct;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.storage.Database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductDAOTest extends CommonDatabaseTest {
    private ProductDAO productDAO;

    private static final int SIZE = 10;
    private static final Comparator<SimpleProduct> productComparator = Comparator.comparingInt(SimpleProduct::getPrice);

    @BeforeEach
    void initialize() throws SQLException, IOException {
        Database database = new Database(DB_FILE);
        database.initialize();

        productDAO = new ProductDAO(database);
    }

    @Test
    void shouldInsert() throws SQLException {
        String name = Faker.getName();
        int price = Faker.getPrice();

        productDAO.insert(name, price);

        List<Product> products = productDAO.fetchAll();
        assertEquals(1, products.size());
        assertEquals(name, products.get(0).getName());
        assertEquals(price, products.get(0).getPrice());
    }

    private List<SimpleProduct> insertBatch() throws SQLException {
        List<SimpleProduct> products = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            SimpleProduct product = Faker.getSimpleProduct();
            products.add(product);
            productDAO.insert(product.getName(), product.getPrice());
        }

        return products;
    }

    @Test
    void shouldFetchSeveral() throws SQLException {
        insertBatch();

        List<Product> products = productDAO.fetchAll();
        assertEquals(SIZE, products.size());
    }

    @Test
    void shouldFetchOne() throws SQLException {
        List<SimpleProduct> products = insertBatch();

        SimpleProduct cheapest = products.stream().min(productComparator)
                .orElseThrow(() -> new IllegalArgumentException("Empty list provided."));
        assertEquals(cheapest.getPrice(), productDAO.fetchCheapest().getPrice());

        SimpleProduct mostExpensive = products.stream().max(productComparator)
                .orElseThrow(() -> new IllegalArgumentException("Empty list provided."));
        assertEquals(mostExpensive.getPrice(), productDAO.fetchMostExpensive().getPrice());
    }

    @Test
    void shouldFetchSum() throws SQLException {
        List<SimpleProduct> products = insertBatch();

        int sum = products.stream()
                .map(SimpleProduct::getPrice)
                .reduce(0, Integer::sum);

        assertEquals(sum, productDAO.sumPrice());
    }

    @Test
    void shouldFetchCount() throws SQLException {
        List<SimpleProduct> products = insertBatch();

        assertEquals(products.size(), productDAO.count());
    }
}
