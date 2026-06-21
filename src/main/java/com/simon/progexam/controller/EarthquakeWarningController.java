package com.simon.progexam.controller;

import com.simon.progexam.DTO.CitizenReportDTO;
import com.simon.progexam.DTO.EarthquakeWarningStatusDTO;
import com.simon.progexam.entity.CitizenReport;
import com.simon.progexam.entity.EarthquakeWarning;
import com.simon.progexam.entity.Reading;
import com.simon.progexam.service.CitizenReportService;
import com.simon.progexam.service.EarthquakeWarningService;
import com.simon.progexam.service.ReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EarthquakeWarningController {
    private final CitizenReportService citizenReportService;
    private final EarthquakeWarningService earthquakeWarningService;
    private final ReadingService readingService;

    @PostMapping("/warnings/{id}/reports")
    public ResponseEntity<CitizenReport>createCitizenReport(Principal principal, @PathVariable Integer id, @RequestBody CitizenReportDTO citizenReportDTO){
        EarthquakeWarning earthquakeWarning = earthquakeWarningService.findEarthquakeWarningById(id);
        CitizenReport createCitizenReport = citizenReportService.createCitizenReport(citizenReportDTO.getIntensity(), earthquakeWarning, principal.getName());

        return ResponseEntity.ok(createCitizenReport);
    }

    @GetMapping("/warnings/{id}/reports/count")
    public Integer showCitizenReportsByEarthquakeWarning(@PathVariable Integer id){
        EarthquakeWarning earthquakeWarning = earthquakeWarningService.findEarthquakeWarningById(id);
        Integer citizenReports = citizenReportService.showCitizenReportsByEarthquakeWarning(earthquakeWarning);
        return citizenReports;

    }

    @GetMapping("/warnings/{id}/reports")
    public List<CitizenReport> showCitizenReportsForSpecificEarthquakeWarning(@PathVariable Integer id){
        EarthquakeWarning earthquakeWarning = earthquakeWarningService.findEarthquakeWarningById(id);
        List<CitizenReport> citizenReports = citizenReportService.showCitizenReportsBySpecificEarthquakeWarning(earthquakeWarning);
        return citizenReports;
    }

    @GetMapping("/warnings")
    public List<EarthquakeWarning> showAllEarthquakeWarnings(){
        return earthquakeWarningService.findAllEarthquakeWarnings();
    }

    @GetMapping("/warnings/active")
    public List<EarthquakeWarning> showAllActiveEarthquakeWarnings(){
        return earthquakeWarningService.findAllActiveEarthquakeWarnings();
    }

    @PostMapping("/warnings/{id}/status")
    public ResponseEntity<EarthquakeWarning> changeEarthquakeWarningStatus(@PathVariable Integer id, @RequestBody EarthquakeWarningStatusDTO status){
        EarthquakeWarning earthquakeWarning = earthquakeWarningService.changeEarthquakeWarningStatus(id, status.getStatus());
        return ResponseEntity.ok(earthquakeWarning);
    }

    @GetMapping("/warnings/{id}/readings")
    public List<Reading> showAllReadingsBySpecificEarthquakeWarning(@PathVariable Integer id){
       EarthquakeWarning earthquakeWarning = earthquakeWarningService.findEarthquakeWarningById(id);
        return readingService.findAllReadingsBySpecificEarthquakeWarning(earthquakeWarning);
    }

    @GetMapping("/warnings/reports")
    public List<CitizenReport> showAllCitizenReports(){
        List<CitizenReport> citizenReport = citizenReportService.getAllCitizenReports();
        return citizenReport;

    }
}


