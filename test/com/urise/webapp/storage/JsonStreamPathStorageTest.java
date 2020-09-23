package com.urise.webapp.storage;

import com.urise.webapp.storage.serializers.JsonStreamSerializer;

public class JsonStreamPathStorageTest extends AbstractStorageTest {
    public JsonStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new JsonStreamSerializer()));
    }
}
