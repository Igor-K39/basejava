package com.urise.webapp.storage.serializers;

import com.urise.webapp.model.*;
import com.urise.webapp.util.LocalDateAdapter;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {
    private final LocalDateAdapter ADAPTER = new LocalDateAdapter();

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
        if (type == SectionType.OBJECTIVE || type == SectionType.PERSONAL) {
            String text = ((TextSection) section).getText();
            dos.writeUTF(text);
        } else if (type == SectionType.ACHIEVEMENT || type == SectionType.QUALIFICATIONS) {
            List<String> strings = ((ListSection) section).getStrings();
            writeCollection(dos, strings, dos::writeUTF);
        } else if (type == SectionType.EXPERIENCE || type == SectionType.EDUCATION) {
            List<Organization> organizations = ((OrganizationSection) section).getOrganizations();
            writeCollection(dos, organizations, organization -> {
                dos.writeUTF(organization.getName());
                dos.writeUTF(organization.getWebsite());

                List<Period> periods = organization.getPeriods();
                writeCollection(dos, periods, period -> {
                    dos.writeUTF(period.getTitle());
                    dos.writeUTF(period.getDescription());
                    dos.writeUTF(ADAPTER.marshal(period.getStartDate()));
                    dos.writeUTF(ADAPTER.marshal(period.getEndDate()));
                });
            });
        }
    }

    private interface Reader<T> {
        void read() throws IOException;
    }

    private <T> void readElements(DataInputStream dis, Reader<T> reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }

    private <T> AbstractSection readSection(DataInputStream dis, SectionType type) throws IOException {
        AbstractSection section;

        if (type == SectionType.OBJECTIVE || type == SectionType.PERSONAL) {
            section = new TextSection(dis.readUTF());
        } else if (type == SectionType.ACHIEVEMENT || type == SectionType.QUALIFICATIONS) {
            List<String> strings = new ArrayList<>();
            readElements(dis, () -> strings.add(dis.readUTF()));
            section = new ListSection(strings);
        } else if (type == SectionType.EXPERIENCE || type == SectionType.EDUCATION) {
            List<Organization> organizations = new ArrayList<>();
            readElements(dis, () -> {
                String name = dis.readUTF();
                String website = dis.readUTF();
                List<Period> periods = new ArrayList<>();

                readElements(dis, () -> {
                    String title = dis.readUTF();
                    String description = dis.readUTF();
                    LocalDate startDate = ADAPTER.unmarshal(dis.readUTF());
                    LocalDate endDate = ADAPTER.unmarshal(dis.readUTF());
                    periods.add(new Period(title, description, startDate, endDate));
                });
                organizations.add(new Organization(name, website, periods));
            });

            section = new OrganizationSection(organizations);
        } else {
            throw new IllegalStateException("Wrong section type: " + type.name());
        }
        return section;
    }
}


