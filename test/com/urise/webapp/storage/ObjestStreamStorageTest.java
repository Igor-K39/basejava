package com.urise.webapp.storage;

import java.io.File;

public class ObjestStreamStorageTest extends AbstractStorageTest {

    public ObjestStreamStorageTest() throws IllegalAccessException {
        super(new ObjectStreamStorage(STORAGE_DIR));
    }
}
