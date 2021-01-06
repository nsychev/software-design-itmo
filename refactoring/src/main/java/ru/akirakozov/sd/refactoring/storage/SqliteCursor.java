package ru.akirakozov.sd.refactoring.storage;

import javax.xml.transform.Result;
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

    public ResultSet executeQueryAll() throws SQLException {
        return statement.executeQuery();
    }

    public <T> T executeQueryOne(ResultConsumer<T> consumer) throws SQLException {
        try (ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return consumer.apply(rs);
            } else {
                return null;
            }
        }
    }

    public <T> List<T> executeQueryAll(ResultConsumer<T> consumer) throws SQLException {
        try (ResultSet rs = statement.executeQuery()) {
            List<T> objects = new ArrayList<>();
            while (rs.next()) {
                objects.add(consumer.apply(rs));
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
