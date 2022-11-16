search()

function search() {
    $.ajax({
        url: "/find_users",
        data: {
            username: getDesiredUsername()
        },
        type: "get",
        async: false
    }).done(updateUsersList)
}

function getDesiredUsername() {
    return document.getElementById("user-search-field").value
}

function updateUsersList(newUsers) {
    clearUsersList()
    newUsers.filter(isUserNotOwner).forEach(addUserToList)
}

function addUserToList(user) {
    document.getElementById("users").innerHTML += getHTMLForUser(user)
}

function getHTMLForUser(user) {
    return `<li class="list-group-item d-flex justify-content-around">
                <p class="w-25" id="username-${user.id}"> ${user.username} </p>
                ${getPermissionLevelSelectHTML(user.id) + getAddOwnerButtonHTML(user.id)}
             </li>`
}

function getPermissionLevelSelectHTML(userId) {
    return `<select class="form-select bg-primary text-white w-50" id="level-${userId}">
                    <option value="VIEW_ONLY_PERMISSION">VIEW ONLY</option>
                    <option value="FULL_PERMISSION">FULL</option>
                </select>`
}

function getAddOwnerButtonHTML(userId) {
    return `<button class="btn btn-success w-25" onclick="addOwner(${userId})">add</button>`
}

function clearUsersList() {
    document.getElementById("users").innerHTML = ""
}

function isUserNotOwner(user) {
    return !isUserOwner(user)
}

function isUserOwner(user) {
    return $.ajax({
            url: "/desk/get/owner_permission",
            data: {
                deskId: [[${param.deskId}]],
                userId: user.id,
                userIdWhoAsks: [[${userId}]]
            },
            type: "get",
            async: false
        }
    ).responseJSON !== "NO_PERMISSIONS"
}

function removeOwner(userId) {
    $.ajax({
        url: "/desk/remove/owner",
        data: {
            deskId: [[${param.deskId}]],
            userId: userId,
            removerId: [[${userId}]]
        },
        success: updateListsAfterOwnerRemoved(userId),
        type: "post",
        async: false
    })
}

function updateListsAfterOwnerRemoved() {
    removeOwnerFromList()
    search()
}

function removeOwnerFromList(userId) {
    document.getElementById(`owner-${userId}`).remove()
}

function addOwner(userId) {
    $.ajax({
        url: "/desk/add/owner",
        data: {
            deskId: [[${param.deskId}]],
            userId: userId,
            permission: getSelectedPermission(userId),
            adderId: [[${userId}]]
        },
        success: updateListsAfterNewOwnerAdded(userId),
        type: "post",
        async: false
    })
}

function updateListsAfterNewOwnerAdded(userId) {
    addOwnerToOwnersList(userId)
    search()
}

function addOwnerToOwnersList(userId) {
    document.getElementById("owners").innerHTML += getOwnerHTML(...getNewOwnerData(userId))
}

function getNewOwnerData(userId) {
    return [userId, getUsername(userId), parsePermission(getSelectedPermission(userId))];
}

function parsePermission(permission) {
    return permission.slice(0, permission.lastIndexOf('_')).replaceAll('_', ' ')
}

function getUsername(userId) {
    return document.getElementById(`username-${userId}`).textContent
}

function getSelectedPermission(userId) {
    return document.getElementById(`level-${userId}`).value
}


function getOwnerHTML(userId, username, permission) {
    return `<li class="list-group-item d-flex justify-content-around" id='owner-${userId}'>
                <p class="w-25">${username}</p>
                <select class="form-select bg-primary text-white w-50" disabled>
                    <option selected>${permission}</option>
                </select>
                <button class="btn btn-danger w-25" onclick='removeOwner(${userId})' id='remove_user-${userId}'> remove </button>
            </li>`
}


/*

function getRemoveOwnerButtonHTML(userId) {
    return `<button class="btn btn-danger w-25" onclick="removeOwner(${userId})">remove</button>`
}


function getPermissionLevelHTML(userId) {
    let permission = $.ajax({
        url: '/desk/get/owner_permission',
        type: 'get',
        data: {
            deskId: [[${param.deskId}]],
            userId: userId,
            userIdWhoAsks: [[${userId}]]
        },
        async: false
    }).responseJSON
    return `<select class="form-select bg-primary text-white w-50" id="level-${userId}" disabled>
                    <option selected>${permission.slice(0, permission.lastIndexOf('_')).replaceAll('_', ' ')}</option>
                </select>`
}

 */