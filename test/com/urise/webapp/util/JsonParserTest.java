package com.urise.webapp.util;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.model.AbstractSection;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.TextSection;
import org.junit.Assert;
import org.junit.Test;

public class JsonParserTest {
    @Test
    public void testResume() {
        Resume resume = ResumeTestData.getFilledResume("uuid1", "Vasily Fortochkin");
        String json = JsonParser.write(resume);
        Resume resumeFromJson = JsonParser.read(json, Resume.class);
        Assert.assertEquals(resume, resumeFromJson);
    }

    @Test
    public void write() {
        AbstractSection section = new TextSection("Objective1");
        String json = JsonParser.write(section, AbstractSection.class);
        System.out.println(json);
        AbstractSection sectionFromJson = JsonParser.read(json, AbstractSection.class);
        Assert.assertEquals(section, sectionFromJson);
    }

}