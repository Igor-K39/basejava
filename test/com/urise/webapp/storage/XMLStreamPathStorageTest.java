package com.urise.webapp.storage;

import com.urise.webapp.storage.serializers.ObjectStreamSerializer;
import com.urise.webapp.storage.serializers.XMLStreamSerializer;

import java.io.File;

public class XMLStreamPathStorageTest extends AbstractStorageTest {
    public XMLStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XMLStreamSerializer()));
    }
}
