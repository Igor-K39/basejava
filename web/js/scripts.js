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

function addOrganizationItem(object, name) {
    let listElement = document.createElement('li');
    let n = name.toString();
    listElement.innerHTML = "<input type='text' size=50 name=" + n + ">"
        + "<input type='button' value='удалить' onclick='removeElement(this.parentElement)'>";
    object.append(listElement);
}