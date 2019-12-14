package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    @Override
    public Resume get(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            return getResume(searchKey);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public void save(Resume resume) {
        Object searchKey = getSearchKey(resume.getUuid());
        if (isExist(searchKey)) {
            throw new ExistStorageException(resume.getUuid());
        } else {
            saveResume(searchKey, resume);
        }
    }

    @Override
    public void update(Resume resume) {
        Object searchKey = getSearchKey(resume.getUuid());
        if (isExist(searchKey)) {
            updateResume(searchKey, resume);
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            deleteResume(searchKey);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    protected abstract Resume getResume(Object key);

    protected abstract void saveResume(Object key, Resume resume);

    protected abstract void updateResume(Object key, Resume resume);

    protected abstract void deleteResume(Object key);

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExist(Object key);
}
