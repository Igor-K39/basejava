<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="css/style.css" rel="stylesheet">
    <title>Resume list</title>
</head>
<body>
    <table>
        <caption>Resume list</caption>
        <tr>
            <th>uuid</th>
            <th>full name</th>
        </tr>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.urise.webapp.model.Resume"/>
            <tr>
                <td>${resume.uuid}</td>
                <td>${resume.fullName}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
