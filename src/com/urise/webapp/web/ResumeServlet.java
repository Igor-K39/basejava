package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.ResumeUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
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
            fillBySection(request, type, resume);
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
                resume = ResumeUtil.getResumeTo(storage.get(uuid));
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

    private void fillBySection(HttpServletRequest request, SectionType type, Resume resume) {
        switch (type) {
            case OBJECTIVE:
            case PERSONAL:
                addTextSection(request, type, resume);
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                addListSection(request, type, resume);
                break;
            case EDUCATION:
            case EXPERIENCE:
                addOrganizationSection(request, type, resume);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void addTextSection(HttpServletRequest request, SectionType type, Resume resume) {
        String content = request.getParameter(type.name().toLowerCase());
        if (content != null && !content.equals("")) {
            resume.addSection(type, new TextSection(content));
        }
    }

    private void addListSection(HttpServletRequest request, SectionType type, Resume resume) {
        List<String> strings = getListOfValues(request, type.name().toLowerCase());
        if (strings != null && strings.size() > 0) {
            resume.addSection(type, new ListSection(strings));
        }
    }

    private void addOrganizationSection(HttpServletRequest request, SectionType type, Resume resume) {
        String prefix = getOrganizationPrefix(type);
        String[] titles = request.getParameterValues(prefix + "Name");
        String[] websites = request.getParameterValues(prefix + "Website");
        String[] positions = request.getParameterValues(prefix + "Position");
        String[] descriptions = request.getParameterValues(prefix + "Description");
        String[] starts = request.getParameterValues(prefix + "Start");
        String[] ends = request.getParameterValues(prefix + "End");
        if (hasNull(titles, positions)) {
            return;
        }
        if (type == SectionType.EDUCATION) {
            descriptions = new String[titles.length];
            Arrays.fill(descriptions, "");
        }
        Map<String, String> jobs = new LinkedHashMap<>();
        Map<String, List<Period>> namedPeriods = new LinkedHashMap<>();
        List<Organization> organizations = new ArrayList<>();

        for (int i = 0; i < titles.length; i++) {
            if (!titles[i].equals("")) {
                jobs.put(getCheckedArrayValue(titles, i), getCheckedArrayValue(websites, i));
                List<Period> periods = namedPeriods.computeIfAbsent(getCheckedArrayValue(titles, i), jobTitle -> new ArrayList<>());
                periods.add(new Period(
                        getCheckedArrayValue(positions, i),
                        getCheckedArrayValue(descriptions, i),
                        getParamDate(starts[i]),
                        getParamDate(ends[i])));
            }
        }
        for (Map.Entry<String, String> job : jobs.entrySet()) {
            String name = job.getKey();
            String website = job.getValue();
            organizations.add(new Organization(name, website, namedPeriods.get(name)));
        }
        if (organizations.size() > 0) {
            OrganizationSection section = new OrganizationSection(organizations);
            resume.addSection(type, section);
        }
    }

    private String getOrganizationPrefix(SectionType type) {
        if (type == SectionType.EXPERIENCE) {
            return "job";
        } else if (type == SectionType.EDUCATION) {
            return "edu";
        } else {
            throw new IllegalArgumentException();
        }
    }

    private LocalDate getParamDate(String value) {
        if (!value.equals("")) {
            return LocalDate.parse(value);
        } else {
            return LocalDate.now();
        }
    }

    private String getCheckedArrayValue(String[] param, int position) {
        if (param == null) {
            return "";
        } else {
            return param[position] != null ? param[position] : "";
        }
    }

    private boolean hasNull(String[]... strings) {
        for (String[] parameter : strings) {
            if (parameter == null) {
                return true;
            }
        }
        return false;
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
