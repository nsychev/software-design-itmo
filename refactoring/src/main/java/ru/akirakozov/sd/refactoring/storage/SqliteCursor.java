package ru.akirakozov.sd.refactoring.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SqliteCursor implements AutoCloseable {
    private final Connection connection;
    private final PreparedStatement statement;

    SqliteCursor(String fileName, String sql) throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        this.statement = connection.prepareStatement(sql);
    }

    public void bindParameters(Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }

    public void executeUpdate() throws SQLException {
        statement.executeUpdate();
    }

    public ResultSet executeQuery() throws SQLException {
        return statement.executeQuery();
    }

    public <T> List<T> executeQuery(Function<ResultSet, T> constructor) throws SQLException {
        try (ResultSet rs = statement.executeQuery()) {
            List<T> objects = new ArrayList<>();
            while (rs.next()) {
                objects.add(constructor.apply(rs));
            }
            return objects;
        }
    }

    @Override
    public void close() throws SQLException {
        statement.close();
        connection.close();
    }
}
