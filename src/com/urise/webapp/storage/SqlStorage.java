package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.process(connection -> {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume WHERE uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name").trim());
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.process(connection -> {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume ORDER BY uuid");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return new ArrayList<>();
            } else {
                List<Resume> resumes = new ArrayList<>();
                do {
                    resumes.add(
                            new Resume(rs.getString("uuid").trim(),
                                       rs.getString("full_name").trim())
                    );
                } while (rs.next());
                return resumes;
            }
        });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.process(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO resume(uuid, full_name) VALUES(?, ?)");
            ps.setString(1, resume.getUuid());
            ps.setString(2, resume.getFullName());
            ps.execute();
            return resume;
        });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.process(connection -> {
            PreparedStatement ps = connection.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?");
            ps.setString(1, resume.getFullName());
            ps.setString(2, resume.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(resume.getUuid());
            }
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.process(connection -> {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM resume WHERE uuid = ?");
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return uuid;
        });
    }

    @SuppressWarnings("SqlWithoutWhere")
    @Override
    public void clear() {
        sqlHelper.process(connection -> {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM resume");
            ps.execute();
            return null;
        });
    }

    @Override
    public int size() {
        return sqlHelper.process(connection -> {
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM resume");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new StorageException("Storage Exception");
            }
            return Integer.parseInt(rs.getString("count"));
        });
    }
}
