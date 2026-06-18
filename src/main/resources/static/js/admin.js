

function showAdminPanel() {
    document.getElementById("content").innerHTML = `
        <h2>Admin Panel</h2>

        <h3>Sensormålinger</h3>
        <button onclick="loadSensorReadings()">Hent målinger</button>
        <button onclick="toggleSection('sensor-readings')">Skjul/Vis</button>
        <pre id="sensor-readings"></pre>

        <h3>Alle varsler</h3>
        <button onclick="loadAllWarnings()">Hent varsler</button>
        <button onclick="toggleSection('all-warnings')">Skjul/Vis</button>
        <pre id="all-warnings"></pre>

        <h3>Ændre status på varsel</h3>
        <input type="number" id="warning-id" placeholder="Varsel ID">
        <select id="new-status">
             <option value="ACTIVE">ACTIVE</option>
             <option value="FALSE_ALARM">FALSE_ALARM</option>
             <option value="NOT_ACTIVE">NOT_ACTIVE</option>
        </select>
        <button onclick="updateWarningStatus()">Opdater status</button>
        <button onclick="toggleSection('status-result')">Skjul/Vis</button>
        <pre id="status-result"></pre>
        
        <h3>Alle brugerrapporteringer</h3>
        <button onclick="loadAllCitizenReports()">Se alle rapporter</button>
        <button onclick="toggleSection('all-citizen-reports')">Skjul/Vis</button>
        <pre id="all-citizen-reports"></pre>
        
        <h3>Brugerrapporteringer for specifikt varsel</h3>
        <input type="number" id="warning-id-reports" placeholder="Varsel ID">
        <button onclick="loadCitizenReportByEarthquakeWarning()">Se rapporter for varsel</button>
        <button onclick="toggleSection('all-reports')">Skjul/Vis</button>
        <pre id="all-reports"></pre>
        
        <h3>Antal brugerrapporteringer for varsel</h3>
        <input type="number" id="warning-id-count" placeholder="Varsel ID">
        <button onclick="loadCitizenReportCountByWarning()">Se antal</button>
        <button onclick="toggleSection('report-count')">Skjul/Vis</button>
        <pre id="report-count"></pre>
        
        <h3>Se målinger ført til varsel</h3>
        <input type="number" id="warning-id-readings" placeholder="Varsel ID">
        <button onclick="loadAllReadingsBySpecificEarthquakeWarning()">Se målinger</button>
        <button onclick="toggleSection('readings-for-warnings')">Skjul/Vis</button>
        <pre id="readings-for-warnings"></pre>
    `;
}

function loadAllCitizenReports(){
    fetchAllCitizenReports()
        .then(data => {
            document.getElementById("all-citizen-reports").textContent = JSON.stringify(data, null, 2);
        });
}

function loadCitizenReportCountByWarning() {
    const id = document.getElementById("warning-id-count").value;
    fetchCitizenReportsByEarthquakeWarning(id)
        .then(data => {
            document.getElementById("report-count").textContent = JSON.stringify(data, null, 2);
        });
}

function loadSensorReadings(){
fetchAllReadings()
    .then(data => {
        document.getElementById("sensor-readings").textContent = JSON.stringify(data, null, 2);
    });
}

function loadAllWarnings() {
    fetchAllWarnings()
        .then(data => {
            document.getElementById("all-warnings").textContent = JSON.stringify(data, null, 2);
        });
}

function loadAllReadingsBySpecificEarthquakeWarning() {
    const id = document.getElementById("warning-id-readings").value;
    fetchAllReadingsBySpecificEarthquakeWarning(id)
        .then(data => {
            document.getElementById("readings-for-warnings").textContent = JSON.stringify(data, null, 2);
        });
}

function loadCitizenReportByEarthquakeWarning() {
    const id = document.getElementById("warning-id-reports").value
    fetchCitizenReportByEarthquakeWarning(id)
        .then(data => {
            document.getElementById("all-reports").textContent = JSON.stringify(data, null, 2);
        });
}

function updateWarningStatus(){
    const status = document.getElementById("new-status").value;
    const id = document.getElementById("warning-id").value;

    fetchChangeEarthquakeWarningStatus(id, status)
        .then(data => {
            document.getElementById("status-result").textContent = JSON.stringify(data, null, 2);
        });
}

function toggleSection(id) {
    const element = document.getElementById(id);
    if (element.style.display == "none") {
        element.style.display = "block"
    } else {
        element.style.display = "none"
    }
}