package com.simon.progexam.service;

import com.simon.progexam.entity.CitizenReport;
import com.simon.progexam.entity.EarthquakeWarning;
import com.simon.progexam.repository.CitizenReportRepository;
import com.simon.progexam.repository.EarthquakeWarningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Ansvar: Håndterer brugerrapporteringer
@Service
@RequiredArgsConstructor
public class CitizenReportService {

    private final CitizenReportRepository citizenReportRepository;

    public CitizenReport createCitizenReport(double intensity, EarthquakeWarning earthquakeWarning){
        CitizenReport citizenReport = new CitizenReport();
        citizenReport.setIntensity(intensity);
        citizenReport.setEarthquakeWarning(earthquakeWarning);
        CitizenReport savedCitizenReport = citizenReportRepository.save(citizenReport);
        return savedCitizenReport;
    }

    public Integer showCitizenReportsByEarthquakeWarning(EarthquakeWarning earthquakeWarning){
        Integer citizenReports = citizenReportRepository.findByEarthquakeWarning(earthquakeWarning).size();
        return citizenReports;

    }

    public List<CitizenReport> showCitizenReportsBySpecificEarthquakeWarning(EarthquakeWarning earthquakeWarning){

        return citizenReportRepository.findByEarthquakeWarning(earthquakeWarning);
    }

    public List<CitizenReport> getAllCitizenReports(){
        return citizenReportRepository.findAll();
    }

}
