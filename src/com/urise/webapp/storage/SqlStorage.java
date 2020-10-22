package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                        rs.getString("uuid").trim(),
                        rs.getString("full_name"));
                do {
                    addContact(rs, resume);
                } while (rs.next());
                return resume;
            }
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeTransactional(connection -> {
            List<Resume> resumes = new ArrayList<>();
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
                    String uuid = rs.getString("uuid").trim();
                    if (!containsUuid(resumes, uuid)) {
                        String fullName = rs.getString("full_name");
                        Resume resume = new Resume(uuid, fullName);
                        resumes.add(resume);
                    }
                    addContact(rs, Objects.requireNonNull(getResumeByUuid(resumes, uuid)));
                } while (rs.next());
                return resumes;
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

            query = "INSERT INTO contact(type, value, resume_uuid) VALUES(?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                for (Map.Entry<ContactType, String> contact : resume.getContacts().entrySet()) {
                    ps.setString(1, contact.getKey().name());
                    ps.setString(2, contact.getValue());
                    ps.setString(3, resume.getUuid());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return null;
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

            query = "UPDATE contact SET value = ? WHERE resume_uuid = ? AND type = ? ";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                    ps.setString(1, entry.getValue());
                    ps.setString(2, resume.getUuid());
                    ps.setString(3, entry.getKey().name());
                    ps.addBatch();
                }
                int[] affectedRows = ps.executeBatch();
                if (containsZero(affectedRows)) {
                    throw new StorageException("Updating contact did not exist");
                }
            }
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeTransactional(connection -> {
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
        sqlHelper.executeTransactional(connection -> {
            String query = "DELETE FROM resume";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                return ps.executeUpdate();
            }
        });
    }

    @Override
    public int size() {
        return sqlHelper.executeTransactional(connection -> {
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

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        ContactType contactType = ContactType.valueOf(rs.getString("type"));
        String contactValue = rs.getString("value");
        resume.getContacts().put(contactType, contactValue);
    }

    private Resume getResumeByUuid(List<Resume> resumes, String uuid) {
        for (Resume resume : resumes) {
            if (resume.getUuid().equals(uuid)) {
                return resume;
            }
        }
        return null;
    }

    private boolean containsUuid(List<Resume> resumes, String uuid) {
        for (Resume resume : resumes) {
            if (resume.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsZero(int[] array) {
        for (int i : array) {
            if (i == 0) {
                return true;
            }
        }
        return false;
    }
}