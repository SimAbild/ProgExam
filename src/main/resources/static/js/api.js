const BACKEND_URL = "http://localhost:8080";

function fetchAllWarnings(credentials) {
    return fetch(BACKEND_URL + "/api/warnings", {
        headers: { "Authorization": "Basic " + credentials }
    }).then(response => response.json());
}

function fetchActiveWarnings(credentials) {
    return fetch(BACKEND_URL + "/api/warnings/active", {
        headers: {"Authorization": "Basic " + credentials}
    }).then(response => response.json());
}

function fetchAllReadings(credentials) {
    return fetch(BACKEND_URL + "/api/sensor-reading-data", {
        headers: {"Authorization": "Basic " + credentials}
    }).then(response => response.json());
}

function fetchAllReadingsBySpecificEarthquakeWarning(credentials, id) {
    return fetch(BACKEND_URL + "/api/warnings/" + id + "/readings", {
        headers: {"Authorization": "Basic " + credentials}
    }).then(response => response.json());
}

function fetchCitizenReportsByEarthquakeWarning(credentials, id) {
    return fetch(BACKEND_URL + "/api/warnings/" + id + "/reports/count", {
        headers: {"Authorization": "Basic " + credentials}
    }).then(response => response.json());
}

function fetchCitizenReportByEarthquakeWarning(credentials, id) {
    return fetch(BACKEND_URL + "/api/warnings/" + id + "/reports", {
        headers: {"Authorization": "Basic " + credentials}
    }).then(response => response.json());
}

function fetchChangeEarthquakeWarningStatus(credentials, id, status) {
    return fetch(BACKEND_URL + "/api/warnings/" + id + "/status", {
        method: "POST",
        headers: {
            "Authorization": "Basic " + credentials,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ status: status })
    }).then(response => response.json());
}

function fetchCreateCitizenReport(credentials, id, intensity) {
    return fetch(BACKEND_URL + "/api/warnings/" + id + "/reports", {
        method: "POST",
        headers: {
            "Authorization": "Basic " + credentials,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ intensity: intensity })
    }).then(response => response.json());
}

function fetchAllCitizenReports(credentials) {
    return fetch(BACKEND_URL + "/api/warnings/reports", {
        headers: {"Authorization": "Basic " + credentials}
    }).then(response => response.json());
}
