


function showUserPanel() {
    document.getElementById("content").innerHTML = `
        <h2>User Panel</h2>

        <h3>Aktive jordskælvsvarsler</h3>
        <button onclick="loadActiveWarnings()">Hent aktive varsler</button>
        <button onclick="toggleSection('active-warnings')">Skjul/Vis</button>
        <pre id="active-warnings"></pre>

        <h3>Opret brugerrapportering</h3>
        <input type="number" id="report-warning-id" placeholder="Varsel ID">
        <input type="number" id="report-intensity" placeholder="Intensitet">
        <button onclick="submitCitizenReport()">Opret rapport</button>
        <pre id="report-result"></pre>
    `;
}

function loadActiveWarnings(){
    fetchActiveWarnings()
        .then(data => {
            document.getElementById("active-warnings").textContent = JSON.stringify(data, null, 2);
        });
}

function submitCitizenReport(){
    const id = document.getElementById("report-warning-id").value
    const intensity = document.getElementById("report-intensity").value

    fetchCreateCitizenReport(id, intensity)
        .then(data => {
            document.getElementById("report-result").textContent = JSON.stringify(data, null, 2);
        });

}