function removeElement(o, prefix) {
    //o - period
    let periods = o.parentElement;
    let organization = periods.parentElement;
    let hidden = document.createElement('input');
    hidden.setAttribute('type', 'hidden');
    hidden.setAttribute('name', prefix + 'Count');

    o.remove();
    organization.lastElementChild.remove();
    hidden.setAttribute('value', (periods.childElementCount - 1).toString());
    organization.append(hidden);
}

function addTextListItem(object, name) {
    let listElement = document.createElement('li');
    let n = name.toString();
    listElement.innerHTML = "<div class='label'>" +
                                "<label>" +
                                    "<input type='text' name='" + n + "' size='70'>" +
                                "</label>" +
                            "</div>" +
                            "<div class='content'>" +
                                "<input type='button' value='удалить' onclick='removeElement(this.parentElement.parentElement)'>" +
                            "</div>";
    object.append(listElement);
}

function addPeriod(object, prefix) {
    let listElement = document.createElement('div');
    let inHtml = "<fieldset>" +
                    "<div class='position table'>" +
                        "<div class='label'>Позиция</div>" +
                        "<div class='content'><label><input type='text' size='50' name='" + prefix + "Position' " +
                            "value=''></label>" +
                        "</div>" +
                    "</div>";

    if (prefix === "job") {
        let duties = "<div class='duties table'>" +
                        "<div class='label'>Обязанности</div>" +
                        "<div class='content'>" +
                            "<label><textarea cols='52' rows='5' name='" + prefix + "Duties'></textarea></label>" +
                        "</div>" +
                     "</div>";
        inHtml += duties;
    }

    inHtml +=   "<div class='period-date table'>" +
                    "<div class='date table'>" +
                        "<div class='label'>Начало</div>" +
                        "<div class='content'>" +
                            "<input type='date' name='" + prefix + "Start' value=''>" +
                        "</div>" +
                    "</div>" +
                    "<div class='date table'>" +
                        "<div class='label'>Конец</div>" +
                        "<div class='content'>" +
                            "<input type='date' name='" + prefix + "End' value=''>" +
                        "</div>" +
                    "</div>" +
                "</div>" +
                "<div class='delete-button table'>" +
                    "<input type='button' value='удалить период' " +
                        "onclick='removeElement(this.parentElement.parentElement.parentElement, \"job\")'>" +
                "</div>" +
            "</fieldset>";

    listElement.className = 'period table';
    listElement.innerHTML = inHtml;

    //object - period-button
    let periods = object.parentElement;
    let organization = periods.parentElement;
    organization.lastElementChild.remove();
    object.remove();
    periods.insertBefore(listElement, periods.firstChild);
    let count = periods.childElementCount;
    let hidden = document.createElement('input');
    hidden.setAttribute('type', 'hidden');
    hidden.setAttribute('name', prefix + 'Count');
    hidden.setAttribute('value', count.toString());
    organization.append(hidden);
    periods.insertBefore(object, periods.firstChild);
}

function addOrganizationItem(object, p) {
    let prefix = p.toString();
    let name = "Организация";
    let listElement = document.createElement('li');
    if (prefix === "edu") {
        name = "Учреждение";
    }
    let inHtml =
        "<div class='org-item'>" +
            "<div class='name table'>" +
                "<div class='label'>" +
                    "<label for='job-name'>" + name + "</label>" +
                "</div>" +
                "<div class='content'>" +
                    "<input type='text' id='job-name' name='"+ prefix + "Name' size='50' value=''>" +
                "</div>" +
            "</div>" +
            "<div class='website table'>" +
                "<div class='label'>" +
                    "<label for='job-website'>Веб-сайт</label>" +
                "</div>" +
                "<div class='content'>" +
                    "<input type='text' id='job-website' name='" + prefix + "Website' size='50' value=''>" +
                "</div>" +
            "</div>" +
            "<div class='periods table' id='job-periods'>" +
                "<div class='period-button table'>" +
                    "<div class='label'>Новый период</div>" +
                    "<div class='content'>" +
                        "<input type='button' value='добавить' " +
                            "onclick='addPeriod(this.parentElement.parentElement, \"" + prefix + "\")'>" +
                    "</div>" +
                "</div>" +
            "</div>" +
            "<div class='delete-button table'>" +
                "<div class='label'>Организация</div>" +
                "<div class='content'>" +
                    "<input type='button' value='удалить' " +
                        "onclick='removeElement(this.parentElement.parentElement.parentElement.parentElement)'>" +
                "</div>" +
            "</div>" +
            "<input type='hidden' name='" + prefix + "Count' value='0'>" +
        "</div>";
        inHtml += "";
        listElement.innerHTML = inHtml;
        object.append(listElement);
}

