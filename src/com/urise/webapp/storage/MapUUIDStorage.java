package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public class MapUUIDStorage extends AbstractStorage<String> {
    private Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Resume doGet(String key) {
        return storage.get(key);
    }

    @Override
    public List<Resume> doGetAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected void doSave(String key, Resume resume) {
        storage.put(key, resume);
    }

    @Override
    protected void doUpdate(String key, Resume resume) {
        storage.put(key, resume);
    }

    @Override
    protected void doDelete(String key) {
        storage.remove(key);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(String key) {
        return storage.containsKey(key);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
