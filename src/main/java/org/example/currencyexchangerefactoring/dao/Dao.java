package org.example.currencyexchangerefactoring.dao;

import org.example.currencyexchangerefactoring.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface Dao<K, E> {
    List<E> findAll();
    E save(E e);
    void update(E e);

    class StatementMaker {
        protected PreparedStatement getStatement(String sqlQuery) throws SQLException {
            Connection connection = ConnectionManager.get();
            return connection.prepareStatement(sqlQuery);
        }
        protected PreparedStatement getStatement(String sqlQuery, int generateKeys) throws SQLException {
            Connection connection = ConnectionManager.get();
            return connection.prepareStatement(sqlQuery, generateKeys);
        }
    }
}
