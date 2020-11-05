<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.TextSection" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.ListSection" %>
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
        <button type="submit">Сохранить</button>
        <button name="cancel" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
