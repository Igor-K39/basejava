package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class Organization {
    private String name;
    private String website;
    private final List<Period> periods;

    public Organization(String name, String website, List<Period> periods) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(website, "website must not be null");
        Objects.requireNonNull(periods, "periods must not be null");
        this.name = name;
        this.website = website;
        this.periods = periods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public String toString() {
        return name + " " + website + periods.toString();
    }
}
