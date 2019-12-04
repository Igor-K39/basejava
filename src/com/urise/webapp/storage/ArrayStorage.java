package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {
    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void saveInStorage(int index, Resume resume) {
        if (getIndex(resume.getUuid()) == -1) {
            storage[size] = resume;
            size++;
        } else {
            System.out.printf("Resume %s already exists\n", resume.getUuid());
        }
    }

    @Override
    protected void removeInStorage(int position) {
        storage[position] = storage[size];
        storage[size] = null;
    }
}