package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializers.Serializer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final Serializer serializer;

    public PathStorage(String directory, Serializer serializer) {
        Objects.requireNonNull(directory, "Directory must not be null");
        Objects.requireNonNull(serializer, "Serializer must not be null");
        this.directory = Paths.get(directory);
        this.serializer = serializer;

        if (!Files.isDirectory(this.directory)) {
            throw new IllegalArgumentException(directory + " is not a directory");
        }
        if (!Files.isWritable(this.directory) || !Files.isReadable(this.directory)) {
            throw new IllegalArgumentException(directory + " is not readable or writable");
        }
    }

    @Override
    protected Resume doGet(Path key) {
        try {
            return serializer.doRead(new BufferedInputStream(new FileInputStream(key.toFile())));
        } catch (IOException e) {
            throw new StorageException("Reading error", getFileName(key), e);
        }
    }

    @Override
    protected List<Resume> doGetAll() {
        return getAllFiles().map(this::doGet).collect(Collectors.toList());
    }

    @Override
    protected void doSave(Path key, Resume resume) {
        try {
            serializer.doWrite(resume, new BufferedOutputStream(new FileOutputStream(key.toFile())));
        } catch (IOException e) {
            throw new StorageException("Writing error", getFileName(key), e);
        }
    }

    @Override
    protected void doUpdate(Path key, Resume resume) {
        doSave(key, resume);
    }

    @Override
    protected void doDelete(Path key) {
        try {
            Files.delete(key);
        } catch (IOException e) {
            throw new StorageException("Deleting error.", getFileName(key), e);
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path key) {
        return Files.exists(key);
    }

    @Override
    public void clear() {
        getAllFiles().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return Objects.requireNonNull(directory.toFile().list()).length;
    }

    private Stream<Path> getAllFiles() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Storage error", e);
        }
    }

    private String getFileName(Path path) {
        return path.getFileName().normalize().toString();
    }
}
