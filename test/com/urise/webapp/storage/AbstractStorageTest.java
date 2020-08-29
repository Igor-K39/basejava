package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.ResumeTestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractStorageTest {
    protected Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final String FULL_NAME_1 = "Григорий Кислин";
    private static final String FULL_NAME_2 = "Иванов Иван";
    private static final String FULL_NAME_3 = "Петров Пётр";
    private static final String FULL_NAME_4 = "Василий Форточкин";
    protected static final Resume RESUME_1 = ResumeTestData.getFilledResume(UUID_1, FULL_NAME_1);
    protected static final Resume RESUME_2 = ResumeTestData.getFilledResume(UUID_2, FULL_NAME_2);
    protected static final Resume RESUME_3 = ResumeTestData.getFilledResume(UUID_3, FULL_NAME_3);
    protected static final Resume RESUME_4 = ResumeTestData.getFilledResume(UUID_4, FULL_NAME_4);
    protected static final List<Resume> RESUME_LIST = Arrays.asList(RESUME_1, RESUME_2, RESUME_3);

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(ResumeTestData.getFilledResume(UUID_1, FULL_NAME_1));
        storage.save(ResumeTestData.getFilledResume(UUID_2, FULL_NAME_2));
        storage.save(ResumeTestData.getFilledResume(UUID_3, FULL_NAME_3));
    }

    @Test
    public void get() {
        Assert.assertEquals(RESUME_1, storage.get(UUID_1));
        Assert.assertEquals(RESUME_2, storage.get(UUID_2));
        Assert.assertEquals(RESUME_3, storage.get(UUID_3));
    }

    @Test
    public void getAllSorted() {
        List<Resume> allSorted = storage.getAllSorted();
        Assert.assertEquals(storage.size(), allSorted.size());
        Assert.assertEquals(RESUME_LIST, allSorted);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        Assert.assertEquals(4, storage.size());
        Assert.assertEquals(RESUME_4, storage.get(UUID_4));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(RESUME_1);
    }

    @Test
    public void update() {
        Resume testResume = ResumeTestData.getFilledResume(UUID_2, "Some Person");
        storage.update(testResume);
        Assert.assertEquals(testResume, storage.get(UUID_2));
        Assert.assertSame(testResume, storage.get(UUID_2));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(ResumeTestData.getFilledResume("dummy", "dummy"));
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_1);
        Assert.assertEquals(2, storage.size());
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }
}