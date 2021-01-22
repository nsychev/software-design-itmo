package ru.akirakozov.sd.refactoring.common;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleProduct {
    String name;
    int price;

    SimpleProduct(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public SimpleProduct(ResultSet rs) throws SQLException {
        this.name = rs.getString("name");
        this.price = rs.getInt("price");
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
