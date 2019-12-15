package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private List<Resume> storage = new ArrayList<>();

    @Override
    protected Resume getResume(Object key) {
        for (Resume resume : storage) {
            if (resume.getUuid().equals(key)) {
                return resume;
            }
        }
        return null;
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        return storage.toArray(resumes);
    }

    @Override
    protected void saveResume(Object key, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void updateResume(Object key, Resume resume) {
        int index = (int) key;
        storage.add(index, resume);
    }

    @Override
    protected void deleteResume(Object key) {
        int index = (int) key;
        storage.remove(index);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(Object key) {
        return key != null;
    }

    @Override
    public int size() {
        return storage.size();
    }
}
