import com.urise.webapp.model.*;

import java.time.LocalDate;
import java.util.*;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume testResume = new Resume("Григорий Кислин");
        Map<ContactType, String> contacts = testResume.getContacts();
        Map<SectionType, Section> sections = testResume.getSections();
        Section position;
        Section personal;
        Section achievement;
        Section qualification;
        Section experience;
        Section education;

        contacts.put(ContactType.MOBILE_PHONE, "+7(921) 855-0482");
        contacts.put(ContactType.SKYPE, "grugory.kislin");
        contacts.put(ContactType.EMAIL, "gkislin@yandex.ru");
        contacts.put(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        contacts.put(ContactType.GITHUB, "https://github.com/gkislin");
        contacts.put(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        contacts.put(ContactType.HOMEPAGE, "http://gkislin.ru/");

        String positionData = "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям";
        position = new TextSection(positionData);
        sections.put(SectionType.OBJECTIVE, position);

        String personalData = "Аналитический склад ума, сильная логика, креативность. Пурист кода и архитектуры.";
        personal = new TextSection(personalData);
        sections.put(SectionType.PERSONAL, personal);

        List<String> achievementsData = new ArrayList<>();
        String textData = "С 2013 года: разработка проектов \"Разработка Web приложения\"," +
                "\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). " +
                "Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". " +
                "Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.";
        achievementsData.add(textData);

        textData = "Реализация двухфакторной аутентификации для онлайн платформы управления проектами " +
                "Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.";
        achievementsData.add(textData);

        textData = "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. " +
                "Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением " +
                "на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, " +
                "интеграция CIFS/SMB java сервера.";
        achievementsData.add(textData);

        textData = "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, " +
                "Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.";
        achievementsData.add(textData);

        textData = "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов " +
                "(SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации " +
                "о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования " +
                "и мониторинга системы по JMX (Jython/ Django).";
        achievementsData.add(textData);

        textData = "Реализация протоколов по приему платежей всех основных платежных системы России " +
                "(Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.";
        achievementsData.add(textData);
        achievement = new ListSection(achievementsData);
        sections.put(SectionType.ACHIEVEMENT, achievement);

        List<String> qualificationData = new ArrayList<>();
        textData = "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2";
        qualificationData.add(textData);

        textData = "Version control: Subversion, Git, Mercury, ClearCase, Perforce";
        qualificationData.add(textData);

        textData = "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle";
        qualificationData.add(textData);

        textData = "MySQL, SQLite, MS SQL, HSQLDB";
        qualificationData.add(textData);

        textData = "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy";
        qualificationData.add(textData);

        textData = "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts";
        qualificationData.add(textData);

        textData = "Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, " +
                "Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, " +
                "GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, " +
                "Selenium (htmlelements)";
        qualificationData.add(textData);

        textData = "Python: Django";
        qualificationData.add(textData);

        textData = "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js";
        qualificationData.add(textData);

        textData = "Scala: SBT, Play2, Specs2, Anorm, Spray, Akka";
        qualificationData.add(textData);

        textData = "Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, " +
                "SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, " +
                "BPMN2, LDAP, OAuth1, OAuth2, JWT.";
        qualificationData.add(textData);

        textData = "Инструменты: Maven + plugin development, Gradle, настройка Ngnix,";
        qualificationData.add(textData);

        textData = "администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, " +
                "iReport, OpenCmis, Bonita, pgBouncer.";
        qualificationData.add(textData);

        textData = "Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, " +
                "архитектурных шаблонов, UML, функционального программирования";
        qualificationData.add(textData);

        textData = "Родной русский, английский \"upper intermediate\"";
        qualificationData.add(textData);
        qualification = new ListSection(qualificationData);
        sections.put(SectionType.QUALIFICATIONS, qualification);

        List<Organization> organizationsData = new ArrayList<>();
        String title = "Автор проекта";
        String description = "Создание, организация и проведение Java онлайн проектов и стажировок.";
        LocalDate startDate = LocalDate.of(2013, 10, 1);
        Period period = new Period(title, description, startDate, LocalDate.now());
        List<Period> periods = Arrays.asList(period);
        Organization o = new Organization("Java Online Projects", "http://javaops.ru/", periods);
        organizationsData.add(o);

        title = "Старший разработчик (backend)";
        description = "Проектирование и разработка онлайн платформы управления проектами Wrike " +
                "(Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). " +
                "Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.";
        startDate = LocalDate.of(2014, 10, 1);
        LocalDate endDate = LocalDate.of(2016, 1, 1);
        period = new Period(title, description, startDate, endDate);
        periods = Arrays.asList(period);
        o = new Organization("Wrikle", "https://www.wrike.com/", periods);
        organizationsData.add(o);

        title = "Java архитектор";
        description = "Организация процесса разработки системы ERP для разных окружений: релизная политика, " +
                "версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование " +
                "системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка " +
                "интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, " +
                "экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера " +
                "документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, " +
                "Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting " +
                "via ssh tunnels, PL/Python";
        startDate = LocalDate.of(2012, 4, 1);
        endDate = LocalDate.of(2014, 10, 1);
        period = new Period(title, description, startDate, endDate);
        periods = Arrays.asList(period);
        o = new Organization("RIT Center", "", periods);
        organizationsData.add(o);

        title = "Ведущий программист";
        description = "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, " +
                "Jasper, Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения для " +
                "администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга. JPA, " +
                "Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5.";
        startDate = LocalDate.of(2010, 12, 1);
        endDate = LocalDate.of(2012, 4, 1);
        period = new Period(title, description, startDate, endDate);
        periods = Arrays.asList(period);
        o = new Organization("Luxoft (Deutsche Bank)", "http://www.luxoft.ru/", periods);
        organizationsData.add(o);

        title = "Ведущий специалист";
        description = "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\" (GlassFish v2.1, " +
                "v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2). Реализация администрирования, " +
                "статистики и мониторинга фреймворка. Разработка online JMX клиента (Python/ Jython, Django, ExtJS)";
        startDate = LocalDate.of(2008, 6, 1);
        endDate = LocalDate.of(2010, 12, 1);
        period = new Period(title, description, startDate, endDate);
        periods = Arrays.asList(period);
        o = new Organization("Yota", "https://www.yota.ru/", periods);
        organizationsData.add(o);

        title = "Разработчик ПО";
        description = "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) " +
                "частей кластерного J2EE приложения (OLAP, Data mining).";
        startDate = LocalDate.of(2007, 3, 1);
        endDate = LocalDate.of(2008, 6, 1);
        period = new Period(title, description, startDate, endDate);
        periods = Arrays.asList(period);
        o = new Organization("Enkata", "http://enkata.com/", periods);
        organizationsData.add(o);

        title = "Разработчик ПО";
        description = "Разработка информационной модели, проектирование интерфейсов, реализация и отладка ПО на " +
                "мобильной IN платформе Siemens @vantage (Java, Unix).";
        startDate = LocalDate.of(2005, 1, 1);
        endDate = LocalDate.of(2007, 2, 1);
        period = new Period(title, description, startDate, endDate);
        periods = Arrays.asList(period);
        o = new Organization("Siemens AG", "https://www.siemens.com/ru/ru/home.html", periods);
        organizationsData.add(o);

        title = "Инженер по аппаратному и программному тестированию";
        description = "Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM).";
        startDate = LocalDate.of(1997, 9, 1);
        endDate = LocalDate.of(2005, 1, 1);
        period = new Period(title, description, startDate, endDate);
        periods = Arrays.asList(period);
        o = new Organization("Alcatel", "http://www.alcatel.ru/", periods);
        organizationsData.add(o);
        experience = new OrganizationSection(organizationsData);
        sections.put(SectionType.EXPERIENCE, experience);

        List<Organization> educationData = new ArrayList<>();
        title = "Functional Programming Principles in Scala\" by Martin Odersky";
        startDate = LocalDate.of(2013, 3, 1);
        endDate = LocalDate.of(2013, 5, 1);
        period = new Period(title, "", startDate, endDate);
        periods = Arrays.asList(period);
        o = new Organization("Coursera", "https://www.coursera.org/course/progfun", periods);
        educationData.add(o);

        title = "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\"";
        startDate = LocalDate.of(2011, 3, 1);
        endDate = LocalDate.of(2011, 4, 1);
        period = new Period(title, "", startDate, endDate);
        periods = Arrays.asList(period);
        o = new Organization("Luxoft", "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366", periods);
        educationData.add(o);

        title = "\t3 месяца обучения мобильным IN сетям (Берлин)";
        startDate = LocalDate.of(2005, 1, 1);
        endDate = LocalDate.of(2005, 4, 1);
        period = new Period(title, "", startDate, endDate);
        periods = Arrays.asList(period);
        o = new Organization("Siemens AG", "http://www.siemens.ru/", periods);
        educationData.add(o);

        title = "6 месяцев обучения цифровым телефонным сетям (Москва)";
        startDate = LocalDate.of(1997, 9, 1);
        endDate = LocalDate.of(1998, 3, 1);
        period = new Period(title, "", startDate, endDate);
        periods = Arrays.asList(period);
        o = new Organization("Alcatel", "http://www.alcatel.ru/", periods);
        educationData.add(o);

        periods = new ArrayList<>();
        title = "Аспирантура (программист С, С++)";
        startDate = LocalDate.of(1993, 9, 1);
        endDate = LocalDate.of(1996, 7, 1);
        period = new Period(title, "", startDate, endDate);
        periods.add(period);

        title = "Инженер (программист Fortran, C)";
        startDate = LocalDate.of(1987, 9, 1);
        endDate = LocalDate.of(1993, 7, 1);
        period = new Period(title, "", startDate, endDate);
        periods.add(period);
        String name = "Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики";
        o = new Organization(name, "http://www.ifmo.ru/", periods);
        educationData.add(o);

        title = "Закончил с отличием";
        startDate = LocalDate.of(1984, 9, 1);
        endDate = LocalDate.of(1987, 6, 1);
        period = new Period(title, "", startDate, endDate);
        periods = Arrays.asList(period);
        o = new Organization("Заочная физико-техническая школа при МФТИ", "http://www.school.mipt.ru/", periods);
        educationData.add(o);

        education = new OrganizationSection(educationData);
        sections.put(SectionType.EDUCATION, education);

        printResume(testResume);
    }

    static void printResume(Resume resume) {
        System.out.println(resume + "\n");
        resume.getContacts().entrySet().forEach(System.out::println);
        System.out.println();
        resume.getSections().entrySet().forEach(System.out::println);
    }
}
