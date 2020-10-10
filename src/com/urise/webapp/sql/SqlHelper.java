package com.urise.webapp.sql;

import com.urise.webapp.exception.ExistStorageException;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T>T process(QueryProcessor<T> processor) {
        try (Connection connection = connectionFactory.getConnection()){
            return processor.process(connection);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new ExistStorageException(e.getMessage());
            } else {
                e.printStackTrace();
            }
        } catch (ExistStorageException e) {
            System.out.println("it's already exist");
        }
        return null;
    }

    public interface QueryProcessor<T> {
        T process(Connection connection) throws SQLException;
    }
}
