# Forbedringer og ændringer efter aflevering

Dette dokument beskriver forbedringer foretaget efter den originale aflevering af SeismicMonitor-projektet.

---

## 1. Redundante Lombok-annotations fjernet fra entities

**Filer:** `entity/CitizenReport.java`, `entity/EarthquakeWarning.java`, `entity/Reading.java`, `entity/Sensor.java`

**Hvad blev ændret:**
Fjernede `@NoArgsConstructor`, `@AllArgsConstructor`, `@Getter` og `@Setter` fra alle entity-klasser, da `@Data` allerede inkluderer disse.

**Hvorfor:**
Clean Code — redundant kode gør det sværere at læse og vedligeholde. `@Data` er en samle-annotation der inkluderer `@Getter`, `@Setter`, `@EqualsAndHashCode`, `@ToString` og `@RequiredArgsConstructor`.

**Før:**
```java
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity
public class Sensor {
```

**Efter:**
```java
@Data
@Entity
public class Sensor {
```

---

## 2. Én brugerrapportering pr. varsel per bruger

**Filer:** `entity/CitizenReport.java`, `repository/CitizenReportRepository.java`, `service/CitizenReportService.java`, `controller/EarthquakeWarningController.java`

**Hvad blev ændret:**
Tilføjede `username`-felt på `CitizenReport`, en ny repository-metode der tjekker kombinationen af bruger og varsel, og henter den aktuelle bruger fra Spring Security via `Principal` i controlleren.

**Hvorfor:**
Opgaven kræver at en bruger kun kan oprette én rapportering per varsel. `Principal` er et Java-objekt Spring Security automatisk udfylder med den loggede brugers oplysninger fra hver request.

**Før:**
```java
// CitizenReport havde ingen username
// createCitizenReport tog ikke bruger i betragtning
public CitizenReport createCitizenReport(double intensity, EarthquakeWarning earthquakeWarning) {
    CitizenReport citizenReport = new CitizenReport();
    citizenReport.setIntensity(intensity);
    citizenReport.setEarthquakeWarning(earthquakeWarning);
    return citizenReportRepository.save(citizenReport);
}
```

**Efter:**
```java
// CitizenReport har nu username-felt
// Repository tjekker kombinationen af bruger + varsel
// Service kaster exception hvis rapporten allerede eksisterer
public CitizenReport createCitizenReport(double intensity, EarthquakeWarning earthquakeWarning, String username) {
    if (citizenReportRepository.existsByEarthquakeWarningAndUsername(earthquakeWarning, username)) {
        throw new IllegalArgumentException("Du har allerede oprettet en rapport for dette varsel");
    }
    CitizenReport citizenReport = new CitizenReport();
    citizenReport.setUsername(username);
    citizenReport.setIntensity(intensity);
    citizenReport.setEarthquakeWarning(earthquakeWarning);
    return citizenReportRepository.save(citizenReport);
}

// Controller henter brugernavnet fra Spring Security
public ResponseEntity<CitizenReport> createCitizenReport(Principal principal, @PathVariable Integer id, @RequestBody CitizenReportDTO dto) {
    ...
    citizenReportService.createCitizenReport(dto.getIntensity(), earthquakeWarning, principal.getName());
}
```

---

## 3. @Transactional på createWarning()

**Fil:** `service/EarthquakeWarningService.java`

**Hvad blev ændret:**
Tilføjede `@Transactional` på `createWarning()` metoden.

**Hvorfor:**
`createWarning()` laver fire databaseskrivninger: én insert på varslet og tre updates på målinger. 
Uden `@Transactional` er hver skrivning sin egen transaktion — hvis en fejler halvvejs, ender databasen i en inkonsistent tilstand med et varsel uden tilknyttede målinger. 
`@Transactional` sikrer at alt lykkes eller ingenting gemmes.

**Før:**
```java
public void createWarning(List<Reading> readings) {
```

**Efter:**
```java
@Transactional
public void createWarning(List<Reading> readings) {
```

---
