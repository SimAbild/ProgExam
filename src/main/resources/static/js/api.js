const BACKEND_URL = "http://localhost:8080";

function fetchAllWarnings(credentials) {
    return fetch(BACKEND_URL + "/api/warnings", {
        headers: { "Authorization": "Basic " + credentials }
    }).then(response => response.json());
}