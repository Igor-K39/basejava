package com.urise.webapp.storage.serializers;

import com.urise.webapp.model.Resume;
import com.urise.webapp.util.JsonParser;

import java.io.*;

public class JsonStreamSerializer implements StreamSerializer {
    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(is)) {
            return JsonParser.read(reader, Resume.class);
        }
    }

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(os)) {
            JsonParser.write(resume, writer);
        }
    }
}
