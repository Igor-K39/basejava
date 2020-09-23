package com.urise.webapp.storage.serializers;

import com.urise.webapp.model.*;
import com.urise.webapp.util.XMLParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XMLStreamSerializer implements StreamSerializer {
    private final XMLParser xmlParser;

    public XMLStreamSerializer() {
        xmlParser = new XMLParser(Resume.class, ListSection.class, TextSection.class, Organization.class,
                OrganizationSection.class, Period.class);
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader reader = new InputStreamReader(is)) {
            return xmlParser.unmarshall(reader);
        }
    }

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            xmlParser.marshall(resume, writer);
        }
    }
}
