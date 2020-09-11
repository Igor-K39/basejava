package com.urise.webapp.exception;

public class StorageException extends RuntimeException {
    private final String uuid;
    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message, Exception cause) {
        super(message, cause);
        this.uuid = null;
    }

    public StorageException(String message, String uuid, Exception cause) {
        super(message, cause);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
