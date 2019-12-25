package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage {
    static final int STORAGE_LIMIT = 10_000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public Resume getResume(Object key) {
        int index = (Integer) key;
        return storage[index];
    }

    @Override
    public List<Resume> getAllResumes() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    @Override
    public void saveResume(Object key, Resume resume) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        int index = (Integer) key;
        saveResume(index, resume);
        size++;
    }

    @Override
    public void updateResume(Object searchKey, Resume resume) {
        int index = (Integer) searchKey;
        storage[index] = resume;
    }

    @Override
    public void deleteResume(Object searchKey) {
        int index = (Integer) searchKey;
        size--;
        removeElement(index);
        storage[size] = null;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    protected boolean isExist(Object key) {
        return (Integer) key >= 0;
    }

    protected abstract void removeElement(int position);

    protected abstract void saveResume(int index, Resume resume);
}
