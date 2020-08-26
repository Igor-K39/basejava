package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private List<Resume> storage = new ArrayList<>();

    @Override
    protected Resume doGet(Integer key) {
        return storage.get(key);
    }

    @Override
    public List<Resume> doGetAll() {
        return new ArrayList<>(storage);
    }

    @Override
    protected void doSave(Integer key, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void doUpdate(Integer key, Resume resume) {
        storage.set(key, resume);
    }

    @Override
    protected void doDelete(Integer key) {
        int index = key;
        storage.remove(index);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(Integer key) {
        return key != null;
    }

    @Override
    public int size() {
        return storage.size();
    }
}
