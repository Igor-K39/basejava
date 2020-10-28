package com.urise.webapp.web;

import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Properties;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        File propertiesFile = new File("..\\conf\\resumes.properties");
        System.out.println(" " + System.getProperty("user.dir"));
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(propertiesFile)) {
                properties.load(is);
                String dbUrl = properties.getProperty("db.url");
                String dbUser = properties.getProperty("db.user");
                String dbPassword = properties.getProperty("db.password");
                storage = new SqlStorage(dbUrl, dbUser, dbPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        request.setAttribute("resumes", storage.getAllSorted());
        request.getRequestDispatcher("/resumes.jsp").forward(request, response);
    }
}
