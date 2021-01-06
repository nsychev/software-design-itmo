package ru.akirakozov.sd.refactoring.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {
    private final int id;
    private final String name;
    private final int price;

    Product(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product fromResultSet(ResultSet rs) throws SQLException {
        return new Product(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("price")
        );
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String toString() {
        return name + "\t" + price;
    }
}
