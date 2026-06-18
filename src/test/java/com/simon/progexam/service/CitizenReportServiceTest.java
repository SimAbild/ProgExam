package com.simon.progexam.service;

import com.simon.progexam.entity.CitizenReport;
import com.simon.progexam.entity.EarthquakeWarning;
import com.simon.progexam.repository.CitizenReportRepository;
import com.simon.progexam.repository.EarthquakeWarningRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitizenReportServiceTest {

    @Mock
    private CitizenReportRepository citizenReportRepository;

    @Mock
    private EarthquakeWarningRepository earthquakeWarningRepository;

    @InjectMocks
    private CitizenReportService citizenReportService;

    @Test
    void createCitizenReport_savesAndReturnsReport() {
        EarthquakeWarning warning = new EarthquakeWarning();
        CitizenReport saved = new CitizenReport();
        saved.setIntensity(3.5);
        saved.setEarthquakeWarning(warning);

        when(citizenReportRepository.save(any(CitizenReport.class))).thenReturn(saved);

        CitizenReport result = citizenReportService.createCitizenReport(3.5, warning);

        assertEquals(3.5, result.getIntensity());
        assertEquals(warning, result.getEarthquakeWarning());
        verify(citizenReportRepository).save(any(CitizenReport.class));
    }

    @Test
    void showCitizenReportsByEarthquakeWarning_returnsCorrectCount() {
        EarthquakeWarning warning = new EarthquakeWarning();
        when(citizenReportRepository.findByEarthquakeWarning(warning)).thenReturn(List.of(new CitizenReport(), new CitizenReport()));

        Integer count = citizenReportService.showCitizenReportsByEarthquakeWarning(warning);

        assertEquals(2, count);
    }

    @Test
    void showCitizenReportsBySpecificEarthquakeWarning_returnsReports() {
        EarthquakeWarning warning = new EarthquakeWarning();
        List<CitizenReport> reports = List.of(new CitizenReport(), new CitizenReport());
        when(citizenReportRepository.findByEarthquakeWarning(warning)).thenReturn(reports);

        List<CitizenReport> result = citizenReportService.showCitizenReportsBySpecificEarthquakeWarning(warning);

        assertEquals(2, result.size());
    }

    @Test
    void getAllCitizenReports_returnsAllReports() {
        when(citizenReportRepository.findAll()).thenReturn(List.of(new CitizenReport(), new CitizenReport(), new CitizenReport()));

        List<CitizenReport> result = citizenReportService.getAllCitizenReports();

        assertEquals(3, result.size());
    }
}
