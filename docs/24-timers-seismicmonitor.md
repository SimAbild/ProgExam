## **24 timers eksamen – Programmering 2** 

## _**3. semester Datamatiker - EK Guldbergsgade**_ 

**Denne eksamensopgave udleveres** : **kl. 9:00 torsdag d. 18. juni 2026** 

## **Besvarelse skal afleveres på Wiseflow senest: kl. 9:00 fredag d. 19. juni 2026** 

Du skal i dette eksamensprojekt bygge en **full-stack REST web-applikation** . Opgaven består af en front-end og en back-end. Back-end skal laves med brug af Spring Boot og MySQL database med tilhørende unittests af funktionaliteten. Front-end skal laves med HTML/CSS og JavaScript. 

## **Eksamensopgaven består af 5 delopgaver.** 

## **Vigtigt:** 

- Læs hele opgaveteksten før du går i gang, så du bedre ved hvor meget tid hver del tager. 

- Det forventes at du under hele eksamensperioden arbejder alene på din besvarelse. Enhver deling af kode eller idéer med andre vil blive betragtet som eksamenssnyd. 

- Når afleveringsfristen har passeret, er det ikke tilladt at push'e ændringer til GitHub. Sørg derfor for at aflevere løbende ved at commit'e og push'e løbende. Vent ikke til der er få minutter tilbage, før du afleverer noget som helst. Afprøv at aflevering virker. 

- Det er tilladt at bruge AI-baserede hjælpeværktøjer som ChatGPT, Claude eller Copilot. Til den efterfølgende mundtlige prøve vil du dog blive bedt om at tilføje rettelser eller udvidelser til din kode, uden andre hjælpemidler end IntelliJ, så vær forberedt på det. 

- Det tæller mere at have en feature der er lavet fra JPA fra backend til frontend så knapperne virker, end at alle undersider eksisterer. Prioriter at lave noget både på frontend og backend så du har noget at vise for alle dele af applikationen. Husk at afprøve med unit tests. 

- Du må ikke uploade til mainbranch i dit GitHub-repository efter kl. 09:00, 24 timer efter du har modtaget opgaven. Dette vil gøre din aflevering ugyldig. Du må ikke have commits til main i dine repositories efter afleveringstidspunktet – men du må gerne lave nye branches, og arbejde videre i dem indtil du skal til eksamen. Du bliver stadig bedømt ud fra hvor meget der var indeholdt i den oprindelige aflevering, men det er nærliggende at bruge lidt tid efterfølgende på finpudsning, justering, fejlretning, eller eventuelle features ud over dem der er beskrevet i opgaven. Brug dog ikke tid på at opfinde helt nye, urelaterede features, men mere på at reviewe din egen kode, og se om der er dele der kunne være skrevet mere solidt, elegant eller fleksibelt. 

## **Case: SeismicMonitor** 

En international beredskabsorganisation ønsker et digitalt system til overvågning af jordrystelser. Organisationen har placeret seismiske sensorer i forskellige områder med risiko for jordskælv. 

Sensorerne registrerer rå målinger, men en enkelt måling betyder ikke nødvendigvis, at der er opstået et jordskælv. Systemet skal derfor kunne modtage målinger fra flere sensorer og afgøre, om målingerne samlet set indikerer et jordskælv. 

Når systemet vurderer, at der er opstået et jordskælv, skal det oprette et **jordskælvsvarsel** med **estimeret epicenter** , og **estimeret magnitude** . 

Almindelige brugere skal kunne se aktive jordskælvsvarsler og registrere, om de har mærket rystelser. Administratorer skal desuden kunne se de rå sensormålinger og følge med i, hvilke målinger der har ført til et varsel. Desuden skal administratorerne sætte et varsel til ikke aktiv, hvis det ikke længere er gældende. 

## **Docker container med seismiske sensorer** 

For at kunne “lade som om” at der er sensorer der sender data, skal der anvendes en Docker container til at simulere sensorer. 

Sensorerne (Docker containeren) sender data ud med jævne mellemrum, og data indeholder information om jordskælvsmålinger. Dataene er i JSON-format og har følgende form: 

```
[
    {
      "readingId": "READ-001",
      "sensorId": "SEN-001",
      "sensorLocation": {
         "latitude": 55.61880,
         "longitude": 11.13720
      },
      "estimatedDistanceToEpicenterKm": 47.31,
      "estimatedMagnitude": 3.4,
      "recordedAt": "2026-05-20T10:15:30"
   },
   // ...
]
```

Listen kan indeholde en eller flere målinger. Når containeren er startet, vil det automatisk begynde at kalde 

```
POST /api/sensor-data
```

hvor containeren antager at din spring-applikation kører på `localhost:8080` og lytter til indkomne anmodninger. 

## **Estimering af Epicenter og magnitude** 

I denne opgave anvendes en forenklet model til estimering af epicenter og magnitude. 

Formålet er ikke at lave en fuldstændig korrekt seismologisk beregning, men at implementere en simpel algoritme til at estimere epicenter og magnitude på baggrund af rå sensordata. 

Du får udleveret et kodeeksempel til estimering af epicenter og en beskrivelse af magnitude-beregningen. Du kan antage at selve algoritmen er black box (dvs. du ikke skal forstå dens indre beregning), men blot kunne anvende det i dit projekt. Der lægges vægt på hvorvidt din løsning giver mulighed for at skifte til en anden beregningsmetode, uden at koden ændres. 

Hver måling giver en **estimeret afstand til epicenteret** , men da retningen ikke er kendt, kan det ikke afgøres hvor epicenteret er ud fra en enkelt måling. Derfor skal der anvendes tre målinger for at kunne bestemme epicenterets koordinater: 

De tre cirklers fællesmængde (dvs. der hvor de tre cirkler krydser) er det **estimerede epicenter** . En Java-implementation af epicenterberegningen kan se således ud (kun metoden gives – se næste side): 

```
// Eksempel på estimation af epicenter
public Location estimate(List<LocationWithDistance> measurements) {
if (measurements.size() < 3) {
throw new IllegalArgumentException(
"Der skal være mindst tre målinger."
);
    }
Location ref = measurements.getFirst().location();
double refLatRad = Math.toRadians(ref.latitude());
double refLonRad = Math.toRadians(ref.longitude());
double earthRadius = 6371.00; // KM
double[] x = new double[3];
double[] y = new double[3];
double[] d = new double[3];
for (int i = 0; i < 3; i++) {
Location loc = measurements.get(i).location();
double latRad = Math.toRadians(loc.latitude());
double lonRad = Math.toRadians(loc.longitude());
x[i] = earthRadius * (lonRad - refLonRad) * Math.cos(refLatRad);
y[i] = earthRadius * (latRad - refLatRad);
d[i] = measurements.get(i).distance(); // km
}
double A = 2 * (x[1] - x[0]);
double B = 2 * (y[1] - y[0]);
double C = d[0] * d[0] - d[1] * d[1] - x[0] * x[0] + x[1] * x[1]
            - y[0] * y[0] + y[1] * y[1];
double D = 2 * (x[2] - x[1]);
double E = 2 * (y[2] - y[1]);
double F = d[1] * d[1] - d[2] * d[2] - x[1] * x[1] + x[2] * x[2]
            - y[1] * y[1] + y[2] * y[2];
double denominator = A * E - B * D;
if (Math.abs(denominator) < 1e-12) {
throw new IllegalArgumentException(
"Målepunkterne giver ingen stabil løsning."
);
    }
double epicenterX = (C * E - B * F) / denominator;
double epicenterY = (A * F - C * D) / denominator;
double epicenterLatRad = refLatRad + epicenterY / earthRadius;
double epicenterLonRad = refLonRad + epicenterX / (earthRadius *
Math.cos(refLatRad));
return new Location(
Math.toDegrees(epicenterLatRad),
Math.toDegrees(epicenterLonRad)
    );
}
// Hvor der er anvendt følgende data beholdere
public record Location(double latitude, double longitude) {}
public record LocationWithDistance(Location location, double distance) {}
```

For at **estimere magnitude** , kan et simpelt gennemsnit af målingerne anvendes. Alternativt kan beregningen benytte et vægtet gennemsnit, hvor målinger tættere på epicenteret vægter højere. 

## **Forretningsregler** 

Sensorerne, som simuleres af Docker-containeren, sender en liste med én til tre målinger i samme request. 

Hvis en `POST /api/sensor-data` request indeholder færre end tre målinger, skal målingerne gemmes, men der må ikke oprettes et jordskælvsvarsel, da epicenter ikke kan bestemmes entydigt. 

Hvis en `POST /api/sensor-data` request indeholder præcis tre gyldige målinger, skal systemet forsøge at beregne epicenter og magnitude. 

Det er ikke et krav at kombinere målinger på tværs af flere requests. Kun målinger i samme request skal kombineres. 

En måling betragtes som gyldig, hvis: 

- sensorLocation indeholder latitude og longitude 

- estimatedDistanceToEpicenterKm er større end 0 

- estimatedMagnitude er større end 0 

- recordedAt kan læses som dato/tid 

Hvis epicenter-beregningen fejler, skal målingerne stadig gemmes, men der må ikke oprettes et jordskælvsvarsel. 

Når systemet vurderer, at tre gyldige sensormålinger indikerer et muligt jordskælv, skal der oprettes et jordskælvsvarsel med status `UNDER_REVIEW` . 

Et varsel med status `UNDER_REVIEW` er endnu ikke synligt for almindelige brugere som et aktivt varsel. Administratoren skal vurdere varslet og kan herefter enten markere det som aktivt eller som falsk alarm. 

Når administratoren vurderer, at der ikke længere er fare, kan et aktivt varsel sættes til `NOT_ACTIVE` . 

## **Tilladte state transitions for et jordskælvsvarsel:** 

- UNDER_REVIEW → ACTIVE: Når admin vurderer, at varslet er gyldigt. 

- UNDER_REVIEW → FALSE_ALARM: Når admin vurderer, at varslet er en falsk alarm. 

- ACTIVE → NOT_ACTIVE: Når admin vurderer, at faren er ovre. 

- **FALSE_ALARM og NOT_ACTIVE er slutstates og kan ikke ændres tilbage** . 

## **Starte Docker containeren** 

For at kunne starte containeren med docker compose skal man benytte følgende `compose.yml` fil: 

services: sensors: image: ghcr.io/osman-butt/exam-seismic-simulator:latest environment: STUDENT_APP_BASE_URL: "http://host.docker.internal:8080" _#      SIMULATOR_INTERVAL_MS: 15000_ extra_hosts: " - "host.docker.internal:host-gateway 

Docker containeren startes med følgende kommando (i terminalen): `docker compose up -d` 

Hvert 15. sekund sendes der simulerede sensormålinger (kan ændres til en anden værdi i `compose.yml` ) mod din applikation. 

Du kan herefter tjekke containerens log (her findes info om de simulerede sensormålinger): `docker compose logs sensors` 

Kan den ikke kontakte din applikation, vil du få nedenstående besked i containerens log: 

## **Bemærk:** 

**Delopgaverne er feature-opdelte. Hver delopgave kan derfor omfatte både JPA/database, backend/REST API, forretningslogik og frontend (HTML/CSS/JavaScript), hvor det er relevant.** 

**Frontenden skal kommunikere med backend via REST API.** 

**Der forventes unit tests af centrale forretningsregler.** 

## **Delopgave 1:** Modtagelse og lagring af sensordata 

Implementér den del af systemet, der kan modtage rå målinger fra de simulerede sensorer (docker container). 

Systemet skal kunne: 

- modtage `POST /api/sensor-data` 

- gemme sensorer og sensormålinger i databasen 

- håndtere requests med 1-3 målinger 

- gemme målinger, selvom der ikke kan oprettes et jordskælvsvarsel 

- give mulighed for at se rå sensormålinger 

## **Delopgave 2:** Oprettelse af jordskælvsvarsler 

Implementér funktionalitet så systemet kan oprette et jordskælvsvarsel, når der modtages præcis tre gyldige målinger i samme request. 

Systemet skal kunne: 

- afgøre om et request indeholder nok målinger til et varsel 

- beregne estimeret epicenter 

- beregne estimeret magnitude 

- oprette et varsel med status `UNDER_REVIEW` 

- koble de relevante sensormålinger til varslet 

## **Delopgave 3:** Varsler og borgerregistreringer 

Implementér funktionalitet så systemets data kan vises og håndteres gennem REST API og en simpel frontend. 

Systemet skal kunne: 

- vise aktive jordskælvsvarsler 

- vise alle jordskælvsvarsler 

- oprette brugerrapportering med intensitet 

- vise antal brugerrapporteringer pr. varsel 

- vise brugerrapporteringer for et varsel 

- ændre status på et varsel til (husk regler for state transitioner): 

   - `FALSE_ALARM` 

   - `ACTIVE` 

   - `NOT_ACTIVE` 

- vise hvilke sensormålinger der førte til et varsel 

## **Delopgave 4: Reverse geocoding med OpenStreetMap** 

Implementér funktionalitet, så systemet kan bestemme et geografisk område for et jordskælvsvarsel ud fra epicentrets koordinater. 

Når systemet har beregnet et epicenter for et jordskælvsvarsel, skal systemet kunne bruge epicentrets længde- og breddegrad til at finde et relevant område, fx bynavn, region eller land. 

Der skal benyttes et eksternt API til dette. Du kan fx bruge OpenStreetMap reverse geocoding. 

## **Eksempel på API-kald:** 

```
GET https://nominatim.openstreetmap.org/reverse?format=json&lat=55.6761&lon=12.5683
```

Det skal være muligt at kunne skifte OpenStreetMap ud med et andet. 

## **Delopgave 5: Security** 

Implementér Spring Security, så funktionerne fra de tidligere delopgaver beskyttes med brugerroller. 

Systemet skal have mindst to roller: 

- `USER` 

- `ADMIN` 

## `USER` skal kunne: 

- se aktive jordskælvsvarsler 

- oprette én brugerrapportering pr. varsel 

## `ADMIN` skal kunne: 

- se rå sensormålinger 

- se alle jordskælvsvarsler 

- ændre status på jordskælvsvarsler 

- se brugerrapporteringer 

- se hvilke målinger der har ført til et varsel 

Følgende endpoint skal stadig være offentligt: 

## `POST /api/sensor-data` 

Det skal være offentligt, fordi Docker-containeren skal kunne sende målinger uden login. 

## **Aflevering** 

Du skal aflevere et Spring Boot-projekt hvor du bruger Spring Web, JPA, H2 og/eller MySQL, samt JUnit tests. Frontend bestående af Javascript, HTML og CSS kan ligge i samme projekt eller et separat. 

Når du opretter dit/dine private repositories, skal du tilføje GitHub-brugere for din klasse som collaborators til dine repositories (husk både frontend og backend, hvis du har to): 

|**Hold**|**GitHub-brugere**|**EK-mail**|
|---|---|---|
|**DATA-GBG-F25A**|osman-butt<br>AnikoZs|OSNB@ek.dk<br>ANIZ@ek.dk|
|**DATA-GBG-F25B**|erlmek<br>mnyborg|ERLM@ek.dk<br>MANY@ek.dk|
|**DATA-GBG-F25C**|per-kiil<br>BjornChristensen<br>JarlTuxen|KIIL@ek.dk<br>BJCH@ek.dk<br>JART@ek.dk|



## **Undlad at pushe til main branch af dine repositories efter afslutningen af 24-timerseksamen, da dette vil gøre afleveringen ugyldig.** 

## **Aflever et dokument på Wiseflow med følgende oplysninger:** 

- Dit fulde navn og EK-email 

- Link til dit private GitHub repository. Skal være privat for at være gyldigt. 

- Kort statusbeskrivelse af opgaveløsning 

- Notér hvis der er dele du ikke har nået. Du må også gerne skrive i hvilken grad din back-end er afprøvet med unit tests. 

