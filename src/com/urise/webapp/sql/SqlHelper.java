package com.urise.webapp.sql;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T>T process(String query, QueryProcessor<T> processor) {
        try (Connection connection = connectionFactory.getConnection()){
            PreparedStatement ps = connection.prepareStatement(query);
            return processor.process(ps);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new ExistStorageException(e.getMessage());
            } else {
                throw new StorageException(e);
            }
        }
    }

    public <T>T executeTransactional(SqlProcessor<T> transaction) {
        try (Connection connection = connectionFactory.getConnection()){
            try {
                connection.setAutoCommit(false);
                T value = transaction.execute(connection);
                connection.commit();
                return value;
            } catch (SQLException evt) {
                connection.rollback();
                throw ExceptionUtil.convertSQLException(evt);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public interface QueryProcessor<T> {
        T process(PreparedStatement ps) throws SQLException;
    }
}
