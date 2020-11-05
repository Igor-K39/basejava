package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getSqlStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String uuid = getCheckedParameterValue(request, "uuid");
        String fullName = getCheckedParameterValue(request, "fullName");
        Resume resume = new Resume(uuid, fullName);
        resume.setFullName(fullName);

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            AbstractSection section = getSection(request, type);
            addCheckedSection(type, section, resume);
        }
        String action = getCheckedParameterValue(request, "storageAction");
        doStorageAction(storage, resume, action);
        response.sendRedirect("resume?uuid=" + uuid + "&action=view");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }

        Resume resume;
        switch (action) {
            case "add":
                resume = new Resume(UUID.randomUUID().toString(), "");
                request.setAttribute("resume", resume);
                request.setAttribute("storageAction", "add");
                request.getRequestDispatcher("WEB-INF/jsp/edit.jsp").forward(request, response);
                return;
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                resume = storage.get(uuid);
                request.setAttribute("resume", resume);
                request.setAttribute("storageAction", "edit");
                request.getRequestDispatcher(
                        ("view".equals(action)
                                ? "/WEB-INF/jsp/view.jsp"
                                : "WEB-INF/jsp/edit.jsp")
                ).forward(request, response);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
    }

    private AbstractSection getSection(HttpServletRequest request, SectionType type) {
        switch (type) {
            case OBJECTIVE:
            case PERSONAL:
                String text = request.getParameter(type.name().toLowerCase());
                return text != null ? new TextSection(text) : null;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                List<String> strings = getListOfValues(request, type.name().toLowerCase());
                return strings != null ? new ListSection(strings) : null;
            case EDUCATION:
            case EXPERIENCE:
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    private void addCheckedSection(SectionType type, AbstractSection section, Resume resume) {
        if (section != null) {
            resume.addSection(type, section);
        }
    }

    private String getCheckedParameterValue(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
        return value;
    }

    private List<String> getListOfValues(HttpServletRequest request, String name) {
        String[] strings = request.getParameterValues(name);
        return strings == null ? null
                : Arrays.stream(strings).filter(string -> !string.equals("")).collect(Collectors.toList());
    }

    private void doStorageAction(Storage storage, Resume resume, String action) {
        switch (action) {
            case "add":
                storage.save(resume);
                return;
            case "edit":
                storage.update(resume);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }
}
