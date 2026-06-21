package com.simon.progexam.service;

import com.simon.progexam.entity.CitizenReport;
import com.simon.progexam.entity.EarthquakeWarning;
import com.simon.progexam.repository.CitizenReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Ansvar: Håndterer brugerrapporteringer
@Service
@RequiredArgsConstructor
public class CitizenReportService {

    private final CitizenReportRepository citizenReportRepository;

    public CitizenReport createCitizenReport(double intensity, EarthquakeWarning earthquakeWarning, String username) {

        if (citizenReportRepository.existsByEarthquakeWarningAndUsername(earthquakeWarning, username)) {
            throw new IllegalArgumentException("Du har allerede oprettet en rapport for dette varsel");
        }

        CitizenReport citizenReport = new CitizenReport();


        citizenReport.setUsername(username);
        citizenReport.setIntensity(intensity);
        citizenReport.setEarthquakeWarning(earthquakeWarning);
        CitizenReport savedCitizenReport = citizenReportRepository.save(citizenReport);
        return savedCitizenReport;
    }

    public Integer showCitizenReportsByEarthquakeWarning(EarthquakeWarning earthquakeWarning) {
        Integer citizenReports = citizenReportRepository.countByEarthquakeWarning(earthquakeWarning);
        return citizenReports;

    }

    public List<CitizenReport> showCitizenReportsBySpecificEarthquakeWarning(EarthquakeWarning earthquakeWarning) {

        return citizenReportRepository.findByEarthquakeWarning(earthquakeWarning);
    }

    public List<CitizenReport> getAllCitizenReports() {
        return citizenReportRepository.findAll();
    }


}
