var credentials = null

let currentRole = null;

function openLogin(role) {
    currentRole = role;
    document.getElementById("login-dialog").showModal();
}

function submitLogin() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    credentials = btoa(username + ":" + password);

    document.getElementById("login-dialog").close();
    document.getElementById("login-section").style.display = "none";

    if (currentRole === "admin") {
        showAdminPanel();
    } else {
        showUserPanel();
    }
}