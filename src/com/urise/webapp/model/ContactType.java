package com.urise.webapp.model;

public enum ContactType {
    MOBILE_PHONE("Tел."),
    SKYPE("Skype") {
        @Override
        public String toHtml(String value) {
            return "<a href='skype:" + value + "' class='skype'>" + value + "</a>";
        }
    },
    EMAIL("e-mail") {
        @Override
        public String toHtml(String value) {
            return getTitle() + ": " + toLink("mailto:" + value, value, "email");
        }
    },

    LINKEDIN("LinkedIn") {
        @Override
        public String toHtml(String value) {
            return toLink(value, getTitle(), "linked-in");
        }
    },

    GITHUB("GitHub") {
        @Override
        public String toHtml(String value) {
            return toLink(value, getTitle(), "github");
        }
    },

    STACKOVERFLOW("Stack Overflow") {
        @Override
        public String toHtml(String value) {
            return toLink(value, getTitle(), "stack-overflow");
        }
    },

    HOMEPAGE("Homepage") {
        @Override
        public String toHtml(String value) {
            return toLink(value);
        }
    };

    private String title;

    public String getTitle() {
        return title;
    }

    ContactType(String title) {
        this.title = title;
    }

    public String toHtml(String value) {
        return (value == null) ? "" : toHtml0(value);
    }

    protected String toHtml0(String value) {
        return title + ": " + value;
    }

    public String toLink(String href) {
        return toLink(href, title);
    }

    public String toLink(String href, String title) {
        return "<a href='" + href + "'>" + title + "</a>";
    }

    public String toLink(String href, String title, String classes) {
        return "<a href='" + href + "' class='" + classes + "'>" + title + "</a>";
    }
}
