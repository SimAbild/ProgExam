package com.simon.progexam.service;

import com.simon.progexam.entity.*;
import com.simon.progexam.repository.EarthquakeWarningRepository;
import com.simon.progexam.repository.ReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Ansvar: Opretter jordskælvsvarsler og kobler sensormålinger til dem

@Service
@RequiredArgsConstructor
public class EarthquakeWarningService {

    private final EarthquakeWarningRepository earthquakeWarningRepository;
    private final ReadingRepository readingRepository;
    private final EpicenterCalculator epicenterCalculator;
    private final GeoLocator geoLocator;

    public void createWarning(List<Reading> readings) {
        EpicenterLocation epicenter = epicenterCalculator.calculate(readings);
        double magnitude = averageMagnitude(readings);

        EarthquakeWarning warning = buildWarning(epicenter, magnitude);
        String geoLocationName = geoLocator.locate(epicenter.getLatitude(), epicenter.getLongitude());
        warning.setGeoLocationName(geoLocationName);
        EarthquakeWarning saved = earthquakeWarningRepository.save(warning);

        linkReadingsToWarning(readings, saved);
    }

    private double averageMagnitude(List<Reading> readings) {
        double total = 0;
        for (Reading reading : readings) {
            total += reading.getEstimatedMagnitude();
        }
        return total / readings.size();
    }

    private EarthquakeWarning buildWarning(EpicenterLocation epicenter, double magnitude) {
        EarthquakeWarning warning = new EarthquakeWarning();
        warning.setStatus(EarthquakeWarningStatus.UNDER_REVIEW);
        warning.setEpicenterLat(epicenter.getLatitude());
        warning.setEpicenterLon(epicenter.getLongitude());
        warning.setMagnitude(magnitude);
        warning.setCreatedAt(LocalDateTime.now());
        return warning;
    }

    private void linkReadingsToWarning(List<Reading> readings, EarthquakeWarning warning) {
        for (Reading reading : readings) {
            reading.setEarthquakeWarning(warning);
            readingRepository.save(reading);
        }
    }

    public EarthquakeWarning findEarthquakeWarningById(Integer id) {
        EarthquakeWarning earthquakeWarning = earthquakeWarningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Varsel ikke fundet"));
        return earthquakeWarning;
    }

    public List<EarthquakeWarning> findAllEarthquakeWarnings() {
        return earthquakeWarningRepository.findAll();
    }

    public List<EarthquakeWarning> findAllActiveEarthquakeWarnings() {
        return earthquakeWarningRepository.findByStatus(EarthquakeWarningStatus.ACTIVE);
    }

    public EarthquakeWarning changeEarthquakeWarningStatus(Integer id, EarthquakeWarningStatus status) {
        EarthquakeWarning earthquakeWarning = findEarthquakeWarningById(id);

        switch (earthquakeWarning.getStatus()) {
            case UNDER_REVIEW -> {
                if (status == EarthquakeWarningStatus.ACTIVE || status == EarthquakeWarningStatus.FALSE_ALARM) {
                    earthquakeWarning.setStatus(status);
                } else {
                    throw new IllegalArgumentException("Ugyldigt statusskift");
                }
            }

            case ACTIVE -> {
                if (status == EarthquakeWarningStatus.NOT_ACTIVE) {
                    earthquakeWarning.setStatus(status);
                } else {
                    throw new IllegalArgumentException("Ugyldigt statusskift");
                }

            }

            default -> {
                throw new IllegalArgumentException("Ugyldigt statusskift");
            }
        }
        earthquakeWarningRepository.save(earthquakeWarning);
        return earthquakeWarning;
    }


}