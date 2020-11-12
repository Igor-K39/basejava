<%@ page import="com.urise.webapp.model.TextSection" %>
<%@ page import="com.urise.webapp.model.ListSection" %>
<%@ page import="com.urise.webapp.model.OrganizationSection" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="../../css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h1>${resume.fullName}</h1>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <hr>
    <table>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <c:set var="name" value="${sectionEntry.key.name()}"/>
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.AbstractSection>"/>
            <tr>
                <td colspan=2>
                    <h2 class="section-name">
                            ${sectionEntry.key.title}
                    </h2>
                </td>
            </tr>
            <c:if test="${name.equals('OBJECTIVE') || name.equals('PERSONAL')}">
                <tr>
                    <td colspan="2" class="padding-left" ${name.equals("OBJECTIVE") ? "class='objective'" : ""}>
                        <%= ((TextSection) sectionEntry.getValue()).getText() %>
                    </td>
                </tr>
            </c:if>
            <c:if test="${name.equals('ACHIEVEMENT') || name.equals('QUALIFICATIONS')}">
                <tr>
                    <td colspan="2" class="padding-left">
                        <ul>
                            <c:forEach var="listItem" items="<%=((ListSection) sectionEntry.getValue()).getStrings()%>">
                                <li>${listItem}</li>
                            </c:forEach>
                        </ul>
                    </td>
                </tr>
            </c:if>
            <c:if test="${name.equals('EXPERIENCE') || name.equals('EDUCATION')}">
                <c:forEach var="organization"
                           items="<%=((OrganizationSection) sectionEntry.getValue()).getOrganizations()%>">
                    <tr>
                        <td colspan="2" class="title-cell"><a class="organization-link"
                                                              href="${organization.website}">${organization.name}</a>
                        </td>
                    </tr>
                    <c:forEach var="period" items="${organization.periods}">
                        <tr>
                            <td class="period-date-view">${DateUtil.getFormattedAsPeriod(period.startDate, period.endDate)}</td>
                            <td class="period-title">${period.title}</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>${period.description}</td>
                        </tr>
                    </c:forEach>

                </c:forEach>
            </c:if>
        </c:forEach>
        <tr>
            <td colspan="3" class="submit">
                <form method="get" action="resume">
                    <input type="submit" value="Назад к списку">
                </form>
            </td>
        </tr>
    </table>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
