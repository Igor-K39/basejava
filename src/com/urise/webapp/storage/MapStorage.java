package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;

public class MapStorage extends AbstractStorage {
    private HashMap<String, Resume> storage = new HashMap<>();

    @Override
    protected Resume getResume(Object key) {
        String uuid = (String) key;
        return storage.get(uuid);
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        return storage.values().toArray(resumes);
    }

    @Override
    protected void saveResume(Object key, Resume resume) {
        String uuid = (String) key;
        storage.put(uuid, resume);
    }

    @Override
    protected void updateResume(Object key, Resume resume) {
        String uuid = (String) key;
        storage.put(uuid, resume);
    }

    @Override
    protected void deleteResume(Object key) {
        String uuid = (String) key;
        storage.remove(uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(Object key) {
        String uuid = (String) key;
        return storage.containsKey(uuid);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
