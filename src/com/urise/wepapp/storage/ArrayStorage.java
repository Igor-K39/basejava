package com.urise.wepapp.storage;

import com.urise.wepapp.model.Resume;
import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        int resumeNumber = findNumberByUUID(r.getUuid());
        if (resumeNumber != -1) {
            storage[resumeNumber] = r;
        } else {
            System.out.println("Resume doesn't exist");
        }
    }

    public void save(Resume r) {
        if (size < 10000) {
            int resumeNUmber = findNumberByUUID(r.getUuid());
            if (resumeNUmber == -1) {
                storage[size] = r;
                size++;
            } else {
                System.out.println("Resume already exists");
            }
        } else {
            System.out.println("The maximum amount of resumes is reached.");
        }
    }

    public Resume get(String uuid) {
        int resumeNumber = findNumberByUUID(uuid);
            if (resumeNumber != -1) {
                return storage[resumeNumber];
            } else {
                System.out.println("Resume doesn't exist");
                return null;
            }
    }

    public void delete(String uuid) {
        int resumeNumber = findNumberByUUID(uuid);
            if (resumeNumber != -1) {
                size--;
                storage[resumeNumber] = storage[size];
                storage[size] = null;
            } else {
                System.out.println("Resume doesn't exist");
            }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int findNumberByUUID(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}