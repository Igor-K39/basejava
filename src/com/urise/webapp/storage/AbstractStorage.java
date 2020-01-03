package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {
    protected final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    @Override
    public Resume get(String uuid) {
        SK searchKey = getCheckedKey(uuid, true);
        return getResume(searchKey);
    }

    public List<Resume> getAllSorted() {
        List<Resume> resumes = getAllResumes();
        resumes.sort(RESUME_COMPARATOR);
        return resumes;
    }

    @Override
    public void save(Resume resume) {
        SK searchKey = getCheckedKey(resume.getUuid(), false);
        saveResume(searchKey, resume);
    }

    @Override
    public void update(Resume resume) {
        SK searchKey = getCheckedKey(resume.getUuid(), true);
        updateResume(searchKey, resume);
    }

    @Override
    public void delete(String uuid) {
        SK searchKey = getCheckedKey(uuid, true);
        deleteResume(searchKey);
    }

    private SK getCheckedKey(String uuid, boolean existence) {
        SK searchKey = getSearchKey(uuid);
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

    protected abstract Resume getResume(SK key);

    protected abstract List<Resume> getAllResumes();

    protected abstract void saveResume(SK key, Resume resume);

    protected abstract void updateResume(SK key, Resume resume);

    protected abstract void deleteResume(SK key);

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean isExist(SK key);
}
