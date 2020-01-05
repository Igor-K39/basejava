package com.urise.webapp.model;

public enum ContactType {
    MOBILE_PHONE("Мобильный тел."),
    SKYPE("Skype"),
    EMAIL("e-mail"),
    LINKEDIN("LinkedIn"),
    GITHUB("GitHub"),
    STACKOVERFLOW("Stack Overflow"),
    HOMEPAGE("Homepage");

    private String title;

    public String getTitle() {
        return title;
    }

    ContactType(String title) {
        this.title = title;
    }
}
