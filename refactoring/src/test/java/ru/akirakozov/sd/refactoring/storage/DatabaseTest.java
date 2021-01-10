package ru.akirakozov.sd.refactoring.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseTest {
    private static final String DB_FILE = "DatabaseTest.db";

    @BeforeAll
    static void cleanup() {
        File dbFile = new File(DB_FILE);
        if (dbFile.exists() ) {
            assertTrue(dbFile.delete());
        }
    }

    @AfterEach
    void cleanupAfterTest() {
        cleanup();
    }

    @Test
    void shouldCreateDatabase() {
        Database database = new Database(DB_FILE);
        assertDoesNotThrow(() -> {
            try (SqliteCursor cursor = database.getCursor("SELECT sqlite_version()")) {
                String version = cursor.executeQueryOne((ResultSet rs) -> rs.getString(1));
                Logger.getGlobal().info("Using sqlite version: " + version);
            }
        });
        File dbFile = new File(DB_FILE);
        assertTrue(dbFile.exists());
    }

    @Test
    void shouldInitialize() {
        Database database = new Database(DB_FILE);
        assertDoesNotThrow(() -> {
            database.initialize();

            try (SqliteCursor cursor = database.getCursor("SELECT * FROM product")) {
                assertEquals(0, cursor.executeQueryAll(ResultSet::getRow).size());
            }
        });
    }

    @Test
    void shouldUpdate() {
        Database database = new Database(DB_FILE);
        assertDoesNotThrow(() -> {
            database.initialize();

            try (SqliteCursor cursor = database.getCursor("INSERT INTO product (name, price) VALUES ('name', 123)")) {
                cursor.executeUpdate();
            }


            try (SqliteCursor cursor = database.getCursor("SELECT * FROM product")) {
                List<SimpleProduct> products = cursor.executeQueryAll(
                        (ResultSet rs) -> new SimpleProduct(rs.getString("name"), rs.getInt("price"))
                );
                assertEquals(1, products.size());
                assertEquals("name", products.get(0).getName());
                assertEquals(123, products.get(0).getPrice());
            }
        });
    }

    @Test
    void updateShouldThrow() {
        Database database = new Database(DB_FILE);
        assertThrows(SQLException.class, () -> {
            // no table product
            try (SqliteCursor cursor = database.getCursor("INSERT INTO product (name, price) VALUES ('eman', 456)")) {
                cursor.executeUpdate();
            }
        });
    }

    @Test
    void selectShouldThrow() {
        Database database = new Database(DB_FILE);
        assertThrows(SQLException.class, () -> {
            try (SqliteCursor cursor = database.getCursor("SELECT * FROM product")) {
                cursor.executeQueryOne((ResultSet rs) -> rs);
            }
        });
    }

    private static class SimpleProduct {
        String name;
        int price;

        SimpleProduct(String name, int price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }
    }
}
