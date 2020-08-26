package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    static final int STORAGE_LIMIT = 10_000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public Resume doGet(Integer key) {
        return storage[key];
    }

    @Override
    public List<Resume> doGetAll() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    @Override
    public void doSave(Integer key, Resume resume) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        doSaveResume(key, resume);
        size++;
    }

    @Override
    public void doUpdate(Integer searchKey, Resume resume) {
        storage[searchKey] = resume;
    }

    @Override
    public void doDelete(Integer searchKey) {
        size--;
        removeElement(searchKey);
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

    protected boolean isExist(Integer key) {
        return key >= 0;
    }

    protected abstract void removeElement(int position);

    protected abstract void doSaveResume(Integer index, Resume resume);
}
