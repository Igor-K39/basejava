package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeTransactional(connection -> {
            String query = ""
                    + "SELECT * "
                    + "FROM resume r "
                    + "LEFT JOIN contact c "
                    + "ON r.uuid = c.resume_uuid "
                    + "WHERE uuid = ?";

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                Resume resume = new Resume(
                        extractValue(rs, "uuid"),
                        extractValue(rs, "full_name"));
                do {
                    addContactIfPresent(rs, resume);
                } while (rs.next());
                return resume;
            }
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute(connection -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            String query = ""
                    + "SELECT * "
                    + "FROM resume "
                    + "LEFT JOIN contact c "
                    + "ON resume.uuid = c.resume_uuid "
                    + "ORDER BY uuid, full_name";

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    return Collections.emptyList();
                }
                do {
                    String uuid = extractValue(rs, "uuid");
                    Resume resume = resumes.computeIfAbsent(
                            uuid,
                            r -> new Resume(uuid, extractValue(rs, "full_name")));
                    addContactIfPresent(rs, resume);
                } while (rs.next());
                return new ArrayList<>(resumes.values());
            }
        });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.executeTransactional(connection -> {
            String query = "INSERT INTO resume(uuid, full_name) VALUES(?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            saveContacts(connection, resume);
            return resume;
        });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.executeTransactional(connection -> {
            String query = "UPDATE resume SET full_name = ? WHERE uuid = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(resume.getUuid());
                }
            }
            deleteContacts(connection, resume.getUuid());
            saveContacts(connection, resume);
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute(connection -> {
            String query = "DELETE FROM resume WHERE uuid = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
            }
            return uuid;
        });
    }

    @SuppressWarnings("SqlWithoutWhere")
    @Override
    public void clear() {
        sqlHelper.execute(connection -> {
            String query = "DELETE FROM resume";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                return ps.executeUpdate();
            }
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute(connection -> {
            String query = "SELECT COUNT(*) FROM resume";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new StorageException("Storage Exception");
                }
                return Integer.parseInt(rs.getString("count"));
            }
        });
    }

    private void addContactIfPresent(ResultSet rs, Resume resume) {
        String contactType = extractValue(rs, "type");
        if (contactType != null) {
            String contactValue = extractValue(rs, "value");
            resume.getContacts().put(ContactType.valueOf(contactType), contactValue);
        }
    }

    private void saveContacts(Connection connection, Resume resume) {
        String query = "INSERT INTO contact(type, value, resume_uuid) VALUES (?, ?, ?)";
        sqlHelper.executeTransactional(connection1 -> {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                for (Map.Entry<ContactType, String> contact : resume.getContacts().entrySet()) {
                    ps.setString(1, contact.getKey().name());
                    ps.setString(2, contact.getValue());
                    ps.setString(3, resume.getUuid());
                    ps.addBatch();
                }
                return ps.executeBatch();
            }
        });
    }

    private void deleteContacts(Connection connection, String uuid) {
        sqlHelper.execute(connection1 -> {
            String query = "DELETE FROM contact WHERE resume_uuid = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, uuid);
                return ps.executeUpdate();
            }
        });
    }

    private String extractValue(ResultSet rs, String columnLabel) {
        try {
            String value = rs.getString(columnLabel);
            if (value != null) {
                return value.trim();
            }
            return null;
        } catch (SQLException e) {
            throw new StorageException("Error while extracting value: " + columnLabel);
        }
    }
}