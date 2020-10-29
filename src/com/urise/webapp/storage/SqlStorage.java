package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.util.JsonParser;

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
            Resume resume;

            String query = "SELECT * FROM resume r WHERE uuid = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, extractValue(rs, "full_name"));
            }

            query = "SELECT * FROM contact WHERE resume_uuid = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addContact(rs, resume);
                }
            }

            query = "SELECT * FROM section WHERE resume_uuid = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, resume.getUuid());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSection(rs, resume);
                }
            }
            return resume;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeTransactional(connection -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();

            String query = "SELECT * FROM resume ORDER BY uuid, full_name";
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
                while (rs.next()) {
                    String uuid = extractValue(rs, "resume_uuid");
                    addContact(rs, resumes.get(uuid));
                }
            }

            query = "SELECT * from section";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = extractValue(rs, "resume_uuid");
                    addSection(rs, resumes.get(uuid));
                }
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
            deleteFromTableByResumeUuid(connection, "contact", resume.getUuid());
            deleteFromTableByResumeUuid(connection, "section", resume.getUuid());
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
                return rs.next() ? rs.getInt(1) : 0;
            }
        });
    }

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        String contactType = extractValue(rs, "type");
        String contactValue = extractValue(rs, "value");
        resume.addContact(ContactType.valueOf(contactType), contactValue);
    }

    private void addSection(ResultSet rs, Resume resume) throws SQLException {
        String type = extractValue(rs, "type");
        String content = extractValue(rs, "content");
        SectionType sectionType = SectionType.valueOf(type);
        AbstractSection section = JsonParser.read(content, AbstractSection.class);
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
                ps.setString(2, JsonParser.write(sectionEntry.getValue(), AbstractSection.class));
                ps.setString(3, resume.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteFromTableByResumeUuid(Connection connection, String tableName, String uuid) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE resume_uuid = ?";
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
}