package ru.akirakozov.sd.refactoring.storage;

import ru.akirakozov.sd.refactoring.utils.ResourceReader;

import java.io.IOException;
import java.sql.SQLException;

public class Database {
    private final String fileName;

    public Database(String fileName) {
        this.fileName = fileName;
    }

    public void initialize() throws IOException, SQLException {
        try (SqliteCursor cursor = getCursor(ResourceReader.read("migrations/init.sql"))) {
            cursor.executeUpdate();
        } catch (SQLException exc) {
            System.err.println("Could not initialize database: " + exc.toString());
            throw exc;
        } catch (IOException exc) {
            System.err.println("Could not read migration file: " + exc.toString());
            throw exc;
        }
    }

    public SqliteCursor getCursor(String sql) throws SQLException {
        return new SqliteCursor(fileName, sql);
    }
}
