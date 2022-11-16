$(document).ready(function () {
    $('#birth-date').mask('00/00/0000');
    $('#phone-number').mask('0000-0000');
})

function createUserListener() {
    createUser(document.getElementById("nickname").value,
        document.getElementById("password").value,
        document.getElementById("email").value)
}

function createUser(nickname, password, email) {
    const request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8080/user/create?nickname=" + nickname + "&email=" + email + "&password=" + password)
    request.onreadystatechange = function (event) {
        if (request.readyState === 4) {
            if (request.status == 200) {
                alert("User was successfully created")
            } else {
                let message = JSON.parse(request.responseText).message
                document.getElementById("error-message").innerText = message
            }
        }
    }
    request.send()
}
//        alert("response: " + JSON.parse(request.response).message)
