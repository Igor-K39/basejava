<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="css/style.css" rel="stylesheet">
    <title>Resume list</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <table class="resume-list">
        <tr class="list-caption">
            <th>Имя</th>
            <th>e-mail</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.urise.webapp.model.Resume"/>
            <tr class="list-item">
                <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                <td>${resume.contacts.get(ContactType.EMAIL)}</td>
                <td><a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png" alt="delete"></a></td>
                <td><a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png" alt="edit"></a></td>
            </tr>
        </c:forEach>
    </table>
    <form method="get" action="resume" style="padding-left: 370px; padding-top: 10px">
        <input type="hidden" name="action" value="add">
        <input type="submit" value="Создать новое резюме">
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
