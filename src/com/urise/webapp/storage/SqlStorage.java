package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public Resume get(String uuid) {
        String query = "SELECT * FROM resume WHERE uuid = ?";
        return sqlHelper.process(query, preparedStatement -> {
            preparedStatement.setString(1, uuid);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name").trim());
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        String query = "SELECT * FROM resume ORDER BY uuid";
        return sqlHelper.process(query, preparedStatement -> {
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                return new ArrayList<>();
            } else {
                List<Resume> resumes = new ArrayList<>();
                do {
                    resumes.add(
                            new Resume(rs.getString("uuid").trim(),
                                    rs.getString("full_name"))
                    );
                } while (rs.next());
                return resumes;
            }
        });
    }

    @Override
    public void save(Resume resume) {
        String query = "INSERT INTO resume(uuid, full_name) VALUES(?, ?)";
        sqlHelper.process(query, preparedStatement -> {
            preparedStatement.setString(1, resume.getUuid());
            preparedStatement.setString(2, resume.getFullName());
            preparedStatement.execute();
            return resume;
        });
    }

    @Override
    public void update(Resume resume) {
        String query = "UPDATE resume SET full_name = ? WHERE uuid = ?";
        sqlHelper.process(query, preparedStatement -> {
            preparedStatement.setString(1, resume.getFullName());
            preparedStatement.setString(2, resume.getUuid());
            if (preparedStatement.executeUpdate() == 0) {
                throw new NotExistStorageException(resume.getUuid());
            }
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        String query = "DELETE FROM resume WHERE uuid = ?";
        sqlHelper.process(query, preparedStatement -> {
            preparedStatement.setString(1, uuid);
            if (preparedStatement.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return uuid;
        });
    }

    @Override
    public void clear() {
        String query = "DELETE FROM resume";
        sqlHelper.process(query, preparedStatement -> {
            preparedStatement.execute();
            return null;
        });
    }

    @Override
    public int size() {
        String query = "SELECT COUNT(*) FROM resume";
        return sqlHelper.process(query, preparedStatement -> {
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                throw new StorageException("Storage Exception");
            }
            return Integer.parseInt(rs.getString("count"));
        });
    }
}
