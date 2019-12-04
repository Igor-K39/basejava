package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {
    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index != -1) {
            storage[index] = resume;
            System.out.printf("Resume %s has been updated\n", resume.getUuid());
        } else {
            System.out.printf("Resume %s doesn't exist\n", resume.getUuid());
        }
    }

    @Override
    public void save(Resume resume) {
        if (size < STORAGE_LIMIT) {
            if (getIndex(resume.getUuid()) == -1) {
                storage[size] = resume;
                size++;
            } else {
                System.out.printf("Resume %s already exists\n", resume.getUuid());
            }
        } else {
            System.out.println("The maximum amount of resumes is reached.");
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            size--;
            storage[index] = storage[size];
            storage[size] = null;
        } else {
            System.out.printf("Resume %s doesn't exist\n", uuid);
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    @Override
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}