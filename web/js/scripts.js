function removeElement(o) {
    o.remove();
}

function addTextRow(o) {
    let row = document.createElement('tr');
    row.innerHTML = "<td><strong> some text field should be here </strong><td>"
        + "<td><input type='button' onclick='removeElement(this.parentElement.parentElement)'></td>";
    o.append(row);
}

function addAchieveListItem(object, name) {
    let listElement = document.createElement('li');
    let n = name.toString();
    listElement.innerHTML = "<input type='text' size=50 name=" + n + ">"
        + "<input type='button' value='удалить' onclick='removeElement(this.parentElement)'>";
    object.append(listElement);
}

function addOrganizationItem() {
    let listElement = document.createElement('li');
    listElement.innerHTML =
        "<table>" +
            "<tr>" +
                "<td class='title-cell'><label for='jobName'>Наименование организации</label></td>" +
                "<td class='title-cell'><input type='text' name='jobName' size='50' id='jobName'></td>" +
            "</tr>" +
            "<tr>" +
                "<td><label for='jobWebsite'>Официальный сайт</label></td>" +
                "<td><input type='text' name='jobWebsite' size='50' id='jobWebsite'></td>" +
            "</tr>" +
            "<tr>" +
                "<td><label for='jobPosition'>Позиция</label></td>" +
                "<td><input type='text' name='jobPosition' size='50' id='jobPosition'></td>" +
            "</tr>" +
            "<tr>" +
                "<td><label for='jobDescription'>Основные обязанности</label></td>" +
                "<td><textarea cols='50' rows='5' name='jobDescription' id='jobDescription'></textarea></td>" +
            "</tr>" +
            "<tr>" +
                "<td><label for='jobStart'>Начало</label></td>" +
                "<td><input type='date' name='jobStart' id='jobStart'></td>" +
            "</tr>" +
            "<tr>" +
                "<td><label for='jobEnd'>Конец</label></td>" +
                "<td><input type='date' name='jobEnd' id='jobEnd'></td>" +
            "</tr>" +
            "<tr>" +
                "<td><input type='button' value='удалить' onclick='removeElement(this.parentElement.parentElement.parentElement.parentElement)'></td>" +
                "<td></td>" +
            "</tr>" +
        "</table>";

    document.getElementById("job-list").append(listElement);
}

function addEducationItem() {
    let listElement = document.createElement('li');
    listElement.innerHTML =
        "<table>" +
            "<tr>" +
                "<td class='title-cell'><label for='eduName'>Наименование учреждения</label></td>" +
                "<td class='title-cell'><input type='text' name='eduName' size='50' id='eduName'></td>" +
            "</tr>" +
            "<tr>" +
                "<td><label for='eduWebsite'>Официальный сайт</label></td>" +
                "<td><input type='text' name='eduWebsite' size='50' id='eduWebsite'></td>" +
            "</tr>" +
            "<tr>" +
                "<td><label for='eduPosition'>Описание</label></td>" +
                "<td><input type='text' name='eduPosition' size='50' id='eduPosition'></td>" +
            "</tr>" +
            "<tr>" +
                "<td><label for='eduStart'>Начало</label></td>" +
                "<td><input type='date' name='eduStart' id='eduStart'></td>" +
            "</tr>" +
            "<tr>" +
                "<td><label for='eduEnd'>Конец</label></td>" +
                "<td><input type='date' name='eduEnd' id='eduEnd'></td>" +
            "</tr>" +
            "<tr>" +
                "<td><input type='button' value='удалить' onclick='removeElement(this.parentElement.parentElement.parentElement.parentElement)'></td>" +
                "<td></td>" +
            "</tr>" +
        "</table>";

    document.getElementById("edu-list").append(listElement);
}

