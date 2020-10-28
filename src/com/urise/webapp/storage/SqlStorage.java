package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
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

            Resume resume;
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, extractValue(rs, "full_name"));
                do {
                    addContactIfPresent(rs, resume);
                } while (rs.next());
            }

            query = "SELECT * FROM section WHERE resume_uuid = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)){
                ps.setString(1, resume.getUuid());
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    return resume;
                }
                do {
                    addSection(rs, resume);
                } while (rs.next());
            }
            return resume;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeTransactional(connection -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            String query = ""
                    + "SELECT * "
                    + "FROM resume "
                    + "ORDER BY uuid, full_name";

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    return Collections.emptyList();
                }
                do {
                    String uuid = extractValue(rs, "uuid");
                    String fullName = extractValue(rs, "full_name");
                    resumes.computeIfAbsent(uuid, r -> new Resume(uuid, fullName));
                } while (rs.next());
            }

            query = "SELECT * FROM contact";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    return new ArrayList<>(resumes.values());
                }
                do {
                    String uuid = extractValue(rs, "resume_uuid");
                    String contactType = extractValue(rs, "type");
                    String value = extractValue(rs, "value");
                    resumes.get(uuid).addContact(ContactType.valueOf(contactType), value);
                } while (rs.next());
            }

            query = "SELECT * from section";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    return new ArrayList<>(resumes.values());
                }
                do {
                    String uuid = extractValue(rs, "resume_uuid");
                    addSection(rs, resumes.get(uuid));
                } while (rs.next());
            }
            return new ArrayList<>(resumes.values());
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
            saveSections(connection, resume);
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
            deleteSections(connection, resume.getUuid());
            saveContacts(connection, resume);
            saveSections(connection, resume);
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

    private void addContactIfPresent(ResultSet rs, Resume resume) throws SQLException {
        String contactType = extractValue(rs, "type");
        if (contactType != null) {
            String contactValue = extractValue(rs, "value");
            resume.addContact(ContactType.valueOf(contactType), contactValue);
        }
    }

    private void addSection(ResultSet rs, Resume resume) throws SQLException {
        String type = extractValue(rs, "type");
        String content = extractValue(rs, "content");
        SectionType sectionType = SectionType.valueOf(type);
        AbstractSection section = createSectionFromString(sectionType, content);
        resume.addSection(sectionType, section);
    }

    private void saveContacts(Connection connection, Resume resume) throws SQLException {
        String query = "INSERT INTO contact(type, value, resume_uuid) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (Map.Entry<ContactType, String> contact : resume.getContacts().entrySet()) {
                ps.setString(1, contact.getKey().name());
                ps.setString(2, contact.getValue());
                ps.setString(3, resume.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void saveSections(Connection connection, Resume resume) throws SQLException {
        String query = "INSERT INTO section(type, content, resume_uuid) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (Map.Entry<SectionType, AbstractSection> sectionEntry : resume.getSections().entrySet()) {
                ps.setString(1, sectionEntry.getKey().name());
                ps.setString(2, convertSectionToString(sectionEntry.getKey(), sectionEntry.getValue()));
                ps.setString(3, resume.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Connection connection, String uuid) throws SQLException {
        String query = "DELETE FROM contact WHERE resume_uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        }
    }

    private void deleteSections(Connection connection, String uuid) throws SQLException {
        String query = "DELETE FROM section WHERE resume_uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        }
    }

    private String extractValue(ResultSet rs, String columnLabel) throws SQLException {
        String value = rs.getString(columnLabel);
        if (value != null) {
            return value.trim();
        }
        return null;
    }

    private String convertSectionToString(SectionType type, AbstractSection section) {
        switch (type) {
            case OBJECTIVE:
            case PERSONAL:
                return ((TextSection) section).getText();
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                return String.join("\n", ((ListSection) section).getStrings());
        }
        return null;
    }

    private AbstractSection createSectionFromString(SectionType type, String content) {
        switch (type) {
            case OBJECTIVE:
            case PERSONAL:
                return new TextSection(content);
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                List<String> strings = Arrays.asList(content.split("\n"));
                return new ListSection(new ArrayList<>(strings));
        }
        return null;
    }
}