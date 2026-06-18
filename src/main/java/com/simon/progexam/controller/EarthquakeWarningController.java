package com.simon.progexam.controller;

import com.simon.progexam.DTO.CitizenReportDTO;
import com.simon.progexam.entity.CitizenReport;
import com.simon.progexam.entity.EarthquakeWarning;
import com.simon.progexam.service.CitizenReportService;
import com.simon.progexam.service.EarthquakeWarningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EarthquakeWarningController {
    private final CitizenReportService citizenReportService;
    private final EarthquakeWarningService earthquakeWarningService;

    @PostMapping("/warnings/{id}/reports")
    public ResponseEntity<CitizenReport>createCitizenReport(@PathVariable Integer id, @RequestBody CitizenReportDTO citizenReportDTO){
        EarthquakeWarning earthquakeWarning = earthquakeWarningService.findEarthquakeWarningById(id);
        CitizenReport createCitizenReport = citizenReportService.createCitizenReport(citizenReportDTO.getIntensity(), earthquakeWarning);

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
}
