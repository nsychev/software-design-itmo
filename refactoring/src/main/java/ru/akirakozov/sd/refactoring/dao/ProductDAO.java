package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.storage.Database;
import ru.akirakozov.sd.refactoring.storage.SqliteCursor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductDAO {
    private final Database database;

    public ProductDAO(Database database) {
        this.database = database;
    }

    public void insert(String name, int price) throws SQLException {
        try (SqliteCursor cursor = database.getCursor("INSERT INTO product (name, price) VALUES (?, ?)")) {
            cursor.bindParameters(name, price);
            cursor.executeUpdate();
        }
    }

    public List<Product> fetchAll() throws SQLException {
        try (SqliteCursor cursor = database.getCursor("SELECT * FROM product")) {
            return cursor.executeQueryAll(Product::fromResultSet);
        }
    }

    private Product fetchOne(String order) throws SQLException {
        try (SqliteCursor cursor = database.getCursor("SELECT * FROM product " + order)) {
            return cursor.executeQueryOne(Product::fromResultSet);
        }
    }

    public Product fetchMostExpensive() throws SQLException {
        return fetchOne("ORDER BY price DESC LIMIT 1");
    }

    public Product fetchCheapest() throws SQLException {
        return fetchOne("ORDER BY price LIMIT 1");
    }

    private int getInteger(String query) throws SQLException {
        try (SqliteCursor cursor = database.getCursor("SELECT " + query + " FROM product")) {
            return cursor.executeQueryOne((ResultSet rs) -> rs.getInt(1));
        }
    }

    public int sumPrice() throws SQLException {
        return getInteger("SUM(price)");
    }

    public int count() throws SQLException {
        return getInteger("COUNT(*)");
    }
}
