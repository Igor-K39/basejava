package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    private final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        SK searchKey = getCheckedKey(uuid, true);
        return doGet(searchKey);
    }

    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> resumes = doGetAll();
        resumes.sort(RESUME_COMPARATOR);
        return resumes;
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        SK searchKey = getCheckedKey(resume.getUuid(), false);
        doSave(searchKey, resume);
    }

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        SK searchKey = getCheckedKey(resume.getUuid(), true);
        doUpdate(searchKey, resume);
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        SK searchKey = getCheckedKey(uuid, true);
        doDelete(searchKey);
    }

    private SK getCheckedKey(String uuid, boolean existence) {
        SK searchKey = getSearchKey(uuid);
        if (existence) {
            if (!isExist(searchKey)) {
                LOG.warning("Resume " + uuid + " not exists");
                throw new NotExistStorageException(uuid);
            }
        } else {
            if (isExist(searchKey)) {
                LOG.warning("Resume " + uuid + " already exists");
                throw new ExistStorageException(uuid);
            }
        }
        return searchKey;
    }

    protected abstract Resume doGet(SK key);

    protected abstract List<Resume> doGetAll();

    protected abstract void doSave(SK key, Resume resume);

    protected abstract void doUpdate(SK key, Resume resume);

    protected abstract void doDelete(SK key);

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean isExist(SK key);
}
