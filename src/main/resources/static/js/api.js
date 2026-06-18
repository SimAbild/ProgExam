const BACKEND_URL = "http://localhost:8080";

function get(url) {
    return fetch(BACKEND_URL + url, {
        headers: {"Authorization": "Basic " + credentials}
    }).then(response => response.json());
}

function fetchAllWarnings() {
    return get("/api/warnings");
}

function fetchActiveWarnings() {
    return get("/api/warnings/active");
}

function fetchAllReadings() {
    return get("/api/sensor-reading-data");
}

function fetchAllReadingsBySpecificEarthquakeWarning(id) {
    return get("/api/warnings/" + id + "/readings");
}

function fetchCitizenReportsByEarthquakeWarning(id) {
    return get("/api/warnings/" + id + "/reports/count");
}

function fetchCitizenReportByEarthquakeWarning( id) {
    return get("/api/warnings/" + id + "/reports");
}

function fetchAllCitizenReports() {
    return get("/api/warnings/reports");
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

