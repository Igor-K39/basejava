<%@ page import="com.urise.webapp.model.*" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
    <link rel='stylesheet' href='css/style.css'>
    <script src='https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js'></script>
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Resume ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method='post' action='resume' enctype='application/x-www-form-urlencoded'>
        <script type='text/javascript' src='js/scripts.js'>
        </script>

        <input type='hidden' name='uuid' value='${resume.uuid}'>
        <input type='hidden' name='storageAction' value='<%=request.getAttribute("storageAction")%>'>

        <div class='text-container table'>
            <div class='label'>
                <strong><label for='full-name'>Имя:</label></strong>
            </div>
            <div class='content'>
                <input id='full-name' type='text' name='fullName' size='30' value='${resume.fullName}'>
            </div>
        </div>
        <hr>

        <div class='text-list'>
            <c:if test="<%= ContactType.values().length > 0 %>">
                <div class='title-cell'>
                    <strong>Контакты</strong>
                </div>
            </c:if>

            <c:forEach var="type" items="<%=ContactType.values()%>">
                <div class='contact table'>
                    <div class='label'>
                        <label for='${type.name()}'>${type.title}</label>
                    </div>
                    <div class='content'>
                        <input id='${type.name()}' type='text' name='${type.name()}' size='30'
                               value='${resume.contacts.get(type)}'>
                    </div>
                </div>
            </c:forEach>
            <hr>
        </div>

        <div class='sections'>
            <div class='text-container table'>
                <c:set var="objective" value="<%= (TextSection) resume.getSection(SectionType.OBJECTIVE) %>"/>
                <div class='label'>
                    <label for='objective'><strong>Позиция</strong></label>
                </div>
                <div class='content'>
                    <input type='text' size='30' name='objective' id='objective'
                           value='${objective.text != null ? objective.text : ''}'>
                </div>
            </div>

            <div class='text-container table'>
                <c:set var="personal" value="<%= (TextSection) resume.getSection(SectionType.PERSONAL) %>"/>
                <div class='label'>
                    <label for='personal'><strong>Личные качества</strong></label>
                </div>
                <div class='content'>
                    <input type='text' size='30' name='personal' id='personal'
                           value='${personal.text != null ? personal.text : ''}'>
                </div>
            </div>

            <div class='text-list table'>
                <div class='header table'>
                    <div class='label'>
                        <strong>Достижения</strong>
                    </div>
                    <div class='content inline-block left'>
                        <input type='button' value='Добавить'
                               onclick='addTextListItem(document.getElementById("achieves"), "achievement")'>
                    </div>
                </div>

                <div class='text-list-items'>
                    <c:set var="achievementSection"
                           value="<%= ((ListSection) resume.getSection(SectionType.ACHIEVEMENT)) %>"/>

                    <ol id='achieves'>
                        <c:if test="${achievementSection != null}">
                            <c:forEach var="achieve" items="${achievementSection.strings}">
                                <li>
                                    <div class='label'>
                                        <label>
                                            <input type='text' size=70 name='achievement' value='${achieve}'>
                                        </label>
                                    </div>
                                    <div class='content'>
                                        <input type='button' value='удалить'
                                               onclick='this.parentElement.parentElement.remove()'>
                                    </div>
                                </li>
                            </c:forEach>
                        </c:if>
                    </ol>
                </div>
            </div>

            <div class='text-list table'>
                <div class='header table'>
                    <div class='label'>
                        <strong>Квалификация</strong>
                    </div>
                    <div class='content inline-block left'>
                        <input type='button' value='Добавить'
                               onclick='addTextListItem(document.getElementById("qualifications"), "qualifications")'>
                    </div>
                </div>

                <div class='text-list-items'>
                    <c:set var="qualificationSection"
                           value="<%= ((ListSection) resume.getSection(SectionType.QUALIFICATIONS)) %>"/>

                    <ol id='qualifications'>
                        <c:if test="${qualificationSection != null}">
                            <c:forEach var="qualification" items="${qualificationSection.strings}">
                                <li>
                                    <div class='label'>
                                        <label>
                                            <input type='text' size=70 name='qualifications'
                                                   value='${qualification}'>
                                        </label>
                                    </div>
                                    <div class='content'>
                                        <input type='button' value='удалить'
                                               onclick='this.parentElement.parentElement.remove()'>
                                    </div>
                                </li>
                            </c:forEach>
                        </c:if>
                    </ol>
                </div>
            </div>
            <hr>
            <div class='title-cell'>
                <strong class='title-cell'>Опыт работы</strong>
                <input type='button' value='Добавить место работы'
                       onclick='addOrganizationItem(document.getElementById("job-list"), "job")'>
            </div>
            <ol id='job-list'>
                <c:set var="experienceSection"
                       value="<%= ((OrganizationSection) resume.getSection(SectionType.EXPERIENCE)) %>"/>

                <c:if test="${experienceSection != null}">
                <c:forEach var="jobItem" items="${experienceSection.organizations}">
                <li>
                    <div class='org-item'>
                        <div class='name table'>
                            <div class='label'><label for='job-name'>Организация</label></div>
                            <div class='content'><input type='text' id='job-name' name='jobName' size='50'
                                                        value='${jobItem.name}'></div>
                        </div>
                        <div class='website table'>
                            <div class='label'><label for='job-website'>Веб-сайт</label></div>
                            <div class='content'><input type='text' id='job-website' name='jobWebsite'
                                                        size='50' value='${jobItem.website}'></div>
                        </div>
                        <div class='periods table' id='job-periods'>
                            <div class='period-button table'>
                                <div class='label'>Новый период</div>
                                <div class='content'><input type='button' value='добавить'
                                                            onclick='addPeriod(this.parentElement.parentElement, "job")'>
                                </div>
                            </div>
                            <c:if test="${jobItem.periods.size() > 0}">
                                <c:forEach var="period" items="${jobItem.periods}">
                                    <div class='period table'>
                                        <fieldset>
                                            <div class='position table'>
                                                <div class='label'>Позиция</div>
                                                <div class='content'><label><input type='text' size='50'
                                                                                   name='jobPosition'
                                                                                   value='${period.title}'>
                                                </label>
                                                </div>
                                            </div>
                                            <div class='duties table'>
                                                <div class='label'>Обязанности</div>
                                                <div class='content'><label><textarea cols='52' rows='5'
                                                                                      name='jobDuties'>${period.description}</textarea>
                                                </label>
                                                </div>
                                            </div>
                                            <div class='period-date table'>
                                                <div class='date table'>
                                                    <div class='label'>Начало</div>
                                                    <div class='content'>
                                                        <input type='date' name='jobStart'
                                                               value='${DateUtil.makeHtmlValue(period.startDate)}'>
                                                    </div>
                                                </div>
                                                <div class='date table'>
                                                    <div class='label'>Конец</div>
                                                    <div class='content'>
                                                        <input type='date' name='jobEnd'
                                                               value='${DateUtil.makeHtmlValue(period.endDate)}'>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class='delete-button'>
                                                <input type='button' value='удалить период'
                                                       onclick='removeElement(this.parentElement.parentElement.parentElement, "job")'>
                                            </div>
                                        </fieldset>
                                    </div>
                                </c:forEach>
                            </c:if>
                        </div>
                        <div class='delete-button table'>
                            <div class='label'>Организация</div>
                            <div class='content'>
                                <input type='button' value='удалить'
                                       onclick='this.parentElement.parentElement.parentElement.parentElement.remove()'>
                            </div>
                        </div>
                        <input type='hidden' name='jobCount' value="${jobItem.periods.size()}">
                        </c:forEach>
                        </c:if>
                    </div>
            </ol>

            <hr>
            <div class='title-cell'>
                <strong class='title-cell'>Образование</strong>
                <input type='button' value='Добавить учреждение'
                       onclick='addOrganizationItem(document.getElementById("edu-list"), "edu")'>
            </div>
            <ol id='edu-list'>
                <c:set var="educationSection"
                       value="<%= ((OrganizationSection) resume.getSection(SectionType.EDUCATION)) %>"/>

                <c:if test="${educationSection != null}">
                <c:forEach var="eduItem" items="${educationSection.organizations}">
                <li>
                    <div class='org-item'>
                        <div class='name table'>
                            <div class='label'><label for='edu-name'>Учреждение</label></div>
                            <div class='content'><input type='text' id='edu-name' name='eduName' size='50'
                                                        value='${eduItem.name}'></div>
                        </div>
                        <div class='website table'>
                            <div class='label'><label for='edu-website'>Веб-сайт</label></div>
                            <div class='content'><input type='text' id='edu-website' name='eduWebsite'
                                                        size='50' value='${eduItem.website}'></div>
                        </div>
                        <div class='periods table' id='edu-periods'>
                            <div class='period-button table'>
                                <div class='label'>Новый период</div>
                                <div class='content'><input type='button' value='добавить'
                                                            onclick='addPeriod(this.parentElement.parentElement, "edu")'>
                                </div>
                            </div>
                            <c:if test="${eduItem.periods.size() > 0}">
                                <c:forEach var="period" items="${eduItem.periods}">
                                    <div class='period table'>
                                        <fieldset>
                                            <div class='position table'>
                                                <div class='label'>Позиция</div>
                                                <div class='content'><label><input type='text' size='50'
                                                                                   name='eduPosition'
                                                                                   value='${period.title}'>
                                                </label>
                                                </div>
                                            </div>
                                            <div class='period-date table'>
                                                <div class='date table'>
                                                    <div class='label'>Начало</div>
                                                    <div class='content'>
                                                        <input type='date' name='eduStart'
                                                               value='${DateUtil.makeHtmlValue(period.startDate)}'>
                                                    </div>
                                                </div>
                                                <div class='date table'>
                                                    <div class='label'>Конец</div>
                                                    <div class='content'>
                                                        <input type='date' name='eduEnd'
                                                               value='${DateUtil.makeHtmlValue(period.endDate)}'>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class='delete-button'>
                                                <input type='button' value='удалить период'
                                                       onclick='removeElement(this.parentElement.parentElement.parentElement, "edu")'>
                                            </div>
                                        </fieldset>
                                    </div>
                                </c:forEach>
                            </c:if>
                        </div>
                        <div class='delete-button table'>
                            <div class='label'>Учреждение</div>
                            <div class='content'>
                                <input type='button' value='удалить'
                                       onclick='this.parentElement.parentElement.parentElement.parentElement.remove()'>
                            </div>
                        </div>
                        <input type='hidden' name='eduCount' value="${eduItem.periods.size()}">
                        </c:forEach>
                        </c:if>
                    </div>
            </ol>
        </div>
        <div class="submit">
            <button type="submit">Сохранить</button>
        </div>
    </form>
    <form method="get" action="resume">
        <input type="submit" value="Назад к списку">
    </form>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
