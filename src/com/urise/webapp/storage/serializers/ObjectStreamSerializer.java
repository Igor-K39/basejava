package com.urise.webapp.storage.serializers;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ObjectStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume resume, OutputStream file) throws IOException {
        try (ObjectOutputStream os = new ObjectOutputStream(file)) {
            os.writeObject(resume);
        }
    }

    @Override
    public Resume doRead(InputStream file) throws IOException {
        try (ObjectInputStream is = new ObjectInputStream(file)){
            return (Resume) is.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Reading error", file.toString());
        }
    }
}
