package ru.akirakozov.sd.refactoring.storage;

import java.sql.SQLException;

public class Database {
    private final String fileName;

    public Database(String fileName) {
        this.fileName = fileName;
    }

    public void initialize() {
        try (SqliteCursor cursor = getCursor("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)")) {
            cursor.executeUpdate();
        } catch (SQLException exc) {
            System.err.println("Could not initialize database: " + exc.toString());
        }
    }

    public SqliteCursor getCursor(String sql) throws SQLException {
        return new SqliteCursor(fileName, sql);
    }
}
