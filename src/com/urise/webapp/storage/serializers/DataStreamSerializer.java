package com.urise.webapp.storage.serializers;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            Map<ContactType, String> contacts = resume.getContacts();
            Map<SectionType, AbstractSection> sections = resume.getSections();

            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            writeCollection(dos, contacts.entrySet(), element -> {
                dos.writeUTF(element.getKey().name());
                dos.writeUTF(element.getValue());
            });
            writeCollection(dos, sections.entrySet(), element -> writeSection(dos, element.getKey(), element.getValue()));
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);

            Map<ContactType, String> contacts = resume.getContacts();
            readElements(dis, () -> {
                ContactType type = ContactType.valueOf(dis.readUTF());
                String value = dis.readUTF();
                contacts.put(type, value);
            });
            Map<SectionType, AbstractSection> sections = resume.getSections();
            readElements(dis, () -> {
                SectionType type = SectionType.valueOf(dis.readUTF());
                AbstractSection section = readSection(dis, type);
                sections.put(type, section);
            });
            return resume;
        }
    }

    private interface Writer<T> {
        void write(T element) throws IOException;
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T element : collection) {
            writer.write(element);
        }
    }

    private void writeSection(DataOutputStream dos, SectionType type, AbstractSection section) throws IOException {
        dos.writeUTF(type.name());
        switch (type) {
            case OBJECTIVE:
            case PERSONAL:
                String text = ((TextSection) section).getText();
                dos.writeUTF(text);
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                List<String> strings = ((ListSection) section).getStrings();
                writeCollection(dos, strings, dos::writeUTF);
                break;
            case EXPERIENCE:
            case EDUCATION:
                writeOrganizationSection(dos, (OrganizationSection) section);
                break;
        }
    }

    private void writeOrganizationSection(DataOutputStream dos, OrganizationSection section) throws IOException {
        List<Organization> organizations = section.getOrganizations();
        writeCollection(dos, organizations, organization -> {
            dos.writeUTF(organization.getName());
            dos.writeUTF(organization.getWebsite());

            List<Period> periods = organization.getPeriods();
            writeCollection(dos, periods, period -> {
                dos.writeUTF(period.getTitle());
                dos.writeUTF(period.getDescription());
                dos.writeUTF(period.getStartDate().toString());
                dos.writeUTF(period.getEndDate().toString());
            });
        });
    }

    private interface Reader {
        void read() throws IOException;
    }

    private void readElements(DataInputStream dis, Reader reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }

    private AbstractSection readSection(DataInputStream dis, SectionType type) throws IOException {
        AbstractSection section;
        switch (type) {
            case OBJECTIVE:
            case PERSONAL:
                section = new TextSection(dis.readUTF());
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                List<String> strings = new ArrayList<>();
                readElements(dis, () -> strings.add(dis.readUTF()));
                section = new ListSection(strings);
                break;
            case EXPERIENCE:
            case EDUCATION:
                section = readOrganizationSection(dis);
                break;
            default:
                throw new IllegalStateException("Wrong section type: " + type.name());
        }
        return section;
    }

    private OrganizationSection readOrganizationSection(DataInputStream dis) throws IOException {
        List<Organization> organizations = new ArrayList<>();
        readElements(dis, () -> {
            String name = dis.readUTF();
            String website = dis.readUTF();
            List<Period> periods = new ArrayList<>();

            readElements(dis, () -> {
                String title = dis.readUTF();
                String description = dis.readUTF();
                LocalDate startDate = LocalDate.parse(dis.readUTF());
                LocalDate endDate = LocalDate.parse(dis.readUTF());
                periods.add(new Period(title, description, startDate, endDate));
            });
            organizations.add(new Organization(name, website, periods));
        });
        return new OrganizationSection(organizations);
    }
}