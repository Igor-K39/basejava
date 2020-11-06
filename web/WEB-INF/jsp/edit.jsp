<%@ page import="com.urise.webapp.model.*" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Resume ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <script type="text/javascript" src="js/scripts.js">
        </script>

        <input type="hidden" name="uuid" value="${resume.uuid}">
        <input type="hidden" name="storageAction" value="<%=request.getAttribute("storageAction")%>">
        <dl>
            <dt><label for="fullName"><strong>Имя:</strong></label></dt>
            <dd><input id="fullName" type="text" name="fullName" size="50" value="${resume.fullName}"></dd>
        </dl>
        <strong>Контакты:</strong>
        <dl>
            <c:forEach var="type" items="<%=ContactType.values()%>">
                <dt><label for="${type.name()}">${type.title}</label></dt>
                <dd><input id="${type.name()}" type="text" name="${type.name()}" size="30"
                           value="${resume.contacts.get(type)}"></dd>
                <br>
            </c:forEach>
        </dl>
        <hr>

        <table>
            <tr>
                <c:set var="objective" value="<%= (TextSection) resume.getSection(SectionType.OBJECTIVE) %>"/>
                <td class="title-cell"><label for="objective"><strong>Позиция</strong></label></td>
                <td class="title-cell"><input type="text" size="80" name="objective" id="objective"
                                              value="${objective.text != null ? objective.text : ""}">
                </td>
            </tr>
            <tr>
                <c:set var="personal" value="<%= (TextSection) resume.getSection(SectionType.PERSONAL) %>"/>
                <td class="title-cell"><label for="personal"><strong>Личные качества</strong></label></td>
                <td class="title-cell"><input type="text" size="80" name="personal" id="personal"
                                              value="${personal.text != null ? personal.text : ""}">
                </td>
            </tr>
            <tr>
                <td class="title-cell"><strong>Достижения</strong></td>
                <td class="title-cell"><input type="button" value="Добавить"
                                              onclick="addAchieveListItem(document.getElementById('achieve-list'), 'achievement')">
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <ol id="achieve-list">
                        <c:set var="achievementSection"
                               value="<%= ((ListSection) resume.getSection(SectionType.ACHIEVEMENT)) %>"/>

                        <c:if test="${achievementSection != null}">
                            <c:forEach var="achieve" items="${achievementSection.strings}">
                                <li><input type="text" size=80 name='achievement' value='${achieve}'>
                                    <input type="button" value="удалить" onclick="removeElement(this.parentElement)">
                                </li>
                            </c:forEach>
                        </c:if>
                    </ol>
                </td>
            </tr>
            <tr>
                <td class="title-cell"><strong>Квалификация</strong></td>
                <td class="title-cell"><input type="button" value="Добавить"
                                              onclick="addAchieveListItem(document.getElementById('qualification-list'), 'qualifications')">
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <ol id="qualification-list">
                        <c:set var="qualificationSection"
                               value="<%= ((ListSection) resume.getSection(SectionType.QUALIFICATIONS))%>"/>

                        <c:if test="${qualificationSection != null}">
                            <c:forEach var="qualification" items="${qualificationSection.strings}">
                                <li><input type="text" size=80 name="qualifications" value="${qualification}">
                                    <input type="button" value="удалить" onclick="removeElement(this.parentElement)">
                                </li>
                            </c:forEach>
                        </c:if>
                    </ol>
                </td>
            </tr>
        </table>
        <hr>
        <div class="title-cell">
            <strong class="title-cell">Опыт работы</strong>
            <input type="button" value="Добавить место работы"
                   onclick="addOrganizationItem()">
        </div>
        <ul id="job-list">
            <c:set var="experienceSection"
                   value="<%= ((OrganizationSection) resume.getSection(SectionType.EXPERIENCE)) %>"/>

            <c:if test="${experienceSection != null}">
                <c:forEach var="jobItem" items="${experienceSection.organizations}">
                    <li>
                        <table>
                            <tr>
                                <td class="title-cell"><label for="jobName">Наименование организации</label></td>
                                <td class="title-cell"><input type="text" name="jobName" size="50" id="jobName"
                                                              value="${jobItem.name}">
                                </td>
                            </tr>
                            <tr>
                                <td><label for="jobWebsite">Официальный сайт</label></td>
                                <td><input type="text" name="jobWebsite" size="50" id="jobWebsite"
                                           value="${jobItem.website}"></td>
                            </tr>
                            <tr>
                                <td><label for="jobPosition">Позиция</label></td>
                                <td><input type="text" name="jobPosition" size="50" id="jobPosition"
                                           value="${jobItem.periods.get(0).title}"></td>
                            </tr>
                            <tr>
                                <td><label for="jobDescription">Основные обязанности</label></td>
                                <td><textarea cols="52" rows="5" name="jobDescription"
                                              id="jobDescription">${jobItem.periods.get(0).description}</textarea>
                                </td>
                            </tr>
                            <tr>
                                <td><label for="jobStart">Начало</label></td>
                                <td><input type="date" name="jobStart" id="jobStart"
                                           value="${DateUtil.makeHtmlValue(jobItem.periods.get(0).startDate)}"></td>
                            </tr>
                            <tr>
                                <td><label for="jobEnd">Конец</label></td>
                                <td><input type="date" name="jobEnd" id="jobEnd"
                                           value="${DateUtil.makeHtmlValue(jobItem.periods.get(0).endDate)}"></td>
                            </tr>
                            <tr>
                                <td><input type="button" value="удалить"
                                           onclick="removeElement(this.parentElement.parentElement.parentElement.parentElement)">
                                </td>
                                <td></td>
                            </tr>
                        </table>
                    </li>
                </c:forEach>
            </c:if>
        </ul>
        <hr>
        <div class="title-cell">
            <strong class="title-cell">Образование</strong>
            <input type="button" value="Добавить учреждение"
                   onclick="addEducationItem()">
        </div>
        <ul id="edu-list">
            <c:set var="educationSection"
                   value="<%= ((OrganizationSection) resume.getSection(SectionType.EDUCATION)) %>"/>

            <c:if test="${experienceSection != null}">
                <c:forEach var="eduItem" items="${educationSection.organizations}">
                    <li>

                        <table>
                            <tr>
                                <td class="title-cell"><label for="eduName">Наименование учреждения</label></td>
                                <td class="title-cell"><input type="text" name="eduName" size="50" id="eduName"
                                                              value="${eduItem.name}">
                                </td>
                            </tr>
                            <tr>
                                <td><label for="eduWebsite">Официальный сайт</label></td>
                                <td><input type="text" name="eduWebsite" size="50" id="eduWebsite"
                                           value="${eduItem.website}"></td>
                            </tr>
                            <tr>
                                <td><label for="eduPosition">Описание</label></td>
                                <td><input type="text" name="eduPosition" size="50" id="eduPosition"
                                           value="${eduItem.periods.get(0).title}"></td>
                            </tr>
                            <tr>
                                <td><label for="eduStart">Начало</label></td>
                                <td><input type="date" name="eduStart" id="eduStart"
                                           value="${DateUtil.makeHtmlValue(eduItem.periods.get(0).startDate)}"></td>
                            </tr>
                            <tr>
                                <td><label for="eduEnd">Конец</label></td>
                                <td><input type="date" name="eduEnd" id="eduEnd"
                                           value="${DateUtil.makeHtmlValue(eduItem.periods.get(0).endDate)}"></td>
                            </tr>
                            <tr>
                                <td><input type="button" value="удалить"
                                           onclick="removeElement(this.parentElement.parentElement.parentElement.parentElement)">
                                </td>
                                <td></td>
                            </tr>
                        </table>
                    </li>
                </c:forEach>
            </c:if>
        </ul>
        <button type="submit">Сохранить</button>
        <button name="cancel" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
