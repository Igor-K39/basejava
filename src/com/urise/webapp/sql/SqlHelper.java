package com.urise.webapp.sql;

import com.urise.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T>T execute(SqlProcessor<T> processor) {
        try (Connection connection = connectionFactory.getConnection()){
            return processor.execute(connection);
        } catch (SQLException e) {
            throw ExceptionUtil.convertSQLException(e);
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

    public interface SqlProcessor<T> {
        T execute(Connection connection) throws SQLException;
    }
}
