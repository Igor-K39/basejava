package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializers.Serializer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final Serializer serializer;

    public FileStorage(File directory, Serializer serializer) {
        Objects.requireNonNull(directory, "The directory to storage cannot be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writable");
        }
        this.directory = directory;
        this.serializer = serializer;
    }

    @Override
    protected Resume doGet(File file) {
        checkIfNull(file);
        try {
            return serializer.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Reading error", file.getName());
        }
    }

    @Override
    protected List<Resume> doGetAll() {
        List<Resume> resumes = new ArrayList<>();
        for (File file : getListFiles()) {
            resumes.add(doGet(file));
        }
        return resumes;
    }

    @Override
    protected void doSave(File file, Resume resume) {
        try {
            if (file.createNewFile()) {
                serializer.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
            } else {
                throw new StorageException("File creating error", resume.getUuid());
            }
        } catch (IOException e) {
            throw new StorageException("Saving error", file.getName(), e);
        }
    }

    @Override
    protected void doUpdate(File file, Resume resume) {
        try {
            serializer.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Updating error", resume.getUuid(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("Deleting error", file.getName());
        }
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    public void clear() {
        File[] listFiles = getListFiles();
        Arrays.stream(listFiles).forEach(this::doDelete);
    }

    @Override
    public int size() {
        return getListFiles().length;
    }

    private void checkIfNull(File file) {
        if (file == null) {
            throw new StorageException("The file or directory must not be null.");
        }
    }

    private File[] getListFiles() {
        File[] listFiles = directory.listFiles();

        if (listFiles != null) {
            return listFiles;
        } else {
            throw new StorageException("I/O Error while getting list of files of " + directory);
        }
    }
}