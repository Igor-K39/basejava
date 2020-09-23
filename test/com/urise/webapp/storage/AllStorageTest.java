package com.urise.webapp.storage;

import com.urise.webapp.storage.serializers.ObjectStreamSerializer;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ListStorageTest.class,
        MapUuidStorageTest.class,
        MapResumeStorageTest.class,
        FileStorageTest.class,
        PathStorageTest.class,
        XMLStreamPathStorageTest.class
})
public class AllStorageTest {
}
