package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MapStorageTest extends AbstractStorageTest {
    public MapStorageTest() {
        super(new MapStorage());
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        Arrays.sort(resumes);
        Assert.assertEquals(resumes.length, storage.size());
        Assert.assertArrayEquals(resumes, RESUME_ARRAY);
    }
}