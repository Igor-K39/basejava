package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage {
    private Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Resume getResume(Object key) {
        return (Resume) key;
    }

    @Override
    public List<Resume> getAllResumes() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected void saveResume(Object key, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void updateResume(Object key, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void deleteResume(Object key) {
        String uuid = ((Resume) key).getUuid();
        storage.remove(uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
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