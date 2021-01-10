package ru.akirakozov.sd.refactoring.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.common.CommonDatabaseTest;
import ru.akirakozov.sd.refactoring.common.SimpleProduct;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseTest extends CommonDatabaseTest {
    private Database database;

    @BeforeEach
    void initialize() {
        this.database = new Database(DB_FILE);
    }

    @Test
    void shouldCreateDatabase() {
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
    void shouldInitialize() throws IOException, SQLException {
        database.initialize();

        try (SqliteCursor cursor = database.getCursor("SELECT * FROM product")) {
            assertEquals(0, cursor.executeQueryAll(ResultSet::getRow).size());
        }
    }

    @Test
    void shouldUpdate() throws IOException, SQLException {
        database.initialize();

        try (SqliteCursor cursor = database.getCursor("INSERT INTO product (name, price) VALUES ('name', 123)")) {
            cursor.executeUpdate();
        }


        try (SqliteCursor cursor = database.getCursor("SELECT * FROM product")) {
            List<SimpleProduct> products = cursor.executeQueryAll(SimpleProduct::new);

            assertEquals(1, products.size());
            assertEquals("name", products.get(0).getName());
            assertEquals(123, products.get(0).getPrice());
        }
    }

    @Test
    void shouldSubstituteParameters() throws IOException, SQLException {
        database.initialize();

        try (SqliteCursor cursor = database.getCursor("INSERT INTO product (name, price) VALUES (?, ?)")) {
            cursor.bindParameters("abcd", 789);
            cursor.executeUpdate();
        }

        try (SqliteCursor cursor = database.getCursor("SELECT * FROM product")) {
            SimpleProduct product = cursor.executeQueryOne(SimpleProduct::new);

            assertNotNull(product);
            assertEquals("abcd", product.getName());
            assertEquals(789, product.getPrice());
        }
    }

    @Test
    void updateShouldThrow() {
        assertThrows(SQLException.class, () -> {
            // no table product
            try (SqliteCursor cursor = database.getCursor("INSERT INTO product (name, price) VALUES ('eman', 456)")) {
                cursor.executeUpdate();
            }
        });
    }

    @Test
    void selectShouldThrow() {
        assertThrows(SQLException.class, () -> {
            try (SqliteCursor cursor = database.getCursor("SELECT * FROM product")) {
                cursor.executeQueryOne((ResultSet rs) -> rs);
            }
        });
    }
}
