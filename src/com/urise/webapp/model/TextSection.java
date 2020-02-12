package com.urise.webapp.model;

import java.util.Objects;

public class TextSection extends Section {
    private final String text;

    public TextSection(String text) {
        Objects.requireNonNull(text, "Text must not be null");
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
