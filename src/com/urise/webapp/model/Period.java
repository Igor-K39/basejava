package com.urise.webapp.model;

import java.time.YearMonth;
import java.util.Objects;

public class Period {
    private String title;
    private String description;
    private YearMonth startDate;
    private YearMonth endDate;

    public Period(String title, String description, YearMonth startDate, YearMonth endDate) {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(description, "description must nut be null");
        Objects.requireNonNull(startDate, "startDate must not be null");
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YearMonth getStartDate() {
        return startDate;
    }

    public void setStartDate(YearMonth startDate) {
        this.startDate = startDate;
    }

    public YearMonth getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonth endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return title + " " + startDate + " " + endDate;
    }
}
