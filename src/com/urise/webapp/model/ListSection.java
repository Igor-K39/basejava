package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class ListSection extends Section {
    private final List<String> strings;

    public ListSection(List<String> strings) {
        Objects.requireNonNull(strings, "strings must not be null");
        this.strings = strings;
    }

    public List<String> getStrings() {
        return strings;
    }

    @Override
    public String toString() {
        return strings.toString();
    }
}
