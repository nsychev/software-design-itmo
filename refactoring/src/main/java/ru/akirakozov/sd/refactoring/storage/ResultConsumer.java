package ru.akirakozov.sd.refactoring.storage;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultConsumer<R> {
    R apply(ResultSet t) throws SQLException;
}
