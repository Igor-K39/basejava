package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    @Override
    public Resume get(String uuid) {
        Object searchKey = getCheckedKey(uuid, true);
        return getResume(searchKey);
    }

    @Override
    public void save(Resume resume) {
        Object searchKey = getCheckedKey(resume.getUuid(), false);
        saveResume(searchKey, resume);
    }

    @Override
    public void update(Resume resume) {
        Object searchKey = getCheckedKey(resume.getUuid(), true);
        updateResume(searchKey, resume);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getCheckedKey(uuid, true);
        deleteResume(searchKey);
    }

    private Object getCheckedKey(String uuid, boolean existence) {
        Object searchKey = getSearchKey(uuid);
        if (existence) {
            if (!isExist(searchKey)) {
                throw new NotExistStorageException(uuid);
            }
        } else {
            if (isExist(searchKey)) {
                throw new ExistStorageException(uuid);
            }
        }
        return searchKey;
    }

    protected abstract Resume getResume(Object key);

    protected abstract void saveResume(Object key, Resume resume);

    protected abstract void updateResume(Object key, Resume resume);

    protected abstract void deleteResume(Object key);

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExist(Object key);
}