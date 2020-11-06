package com.urise.webapp.util;

import com.urise.webapp.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResumeUtil {
    public static Resume getResumeTo(Resume resume) {
        OrganizationSection experience = (OrganizationSection) resume.getSection(SectionType.EXPERIENCE);
        if (experience != null) {
            resume.addSection(SectionType.EXPERIENCE, makeOrganizationSectionTo(experience));
        }
        OrganizationSection education = (OrganizationSection) resume.getSection(SectionType.EDUCATION);
        if (education != null) {
            resume.addSection(SectionType.EDUCATION, makeOrganizationSectionTo(education));
        }
        return resume;
    }

    private static OrganizationSection makeOrganizationSectionTo(OrganizationSection section) {
        List<Organization> organizations = new ArrayList<>();
        for (Organization item : section.getOrganizations()) {
            for (Period period : item.getPeriods()) {
                organizations.add(new Organization(item.getName(), item.getWebsite(), new ArrayList<>(Collections.singletonList(period))));
            }
        }
        return new OrganizationSection(organizations);
    }
}
