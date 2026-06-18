package com.simon.progexam.service;

import com.simon.progexam.entity.*;
import com.simon.progexam.repository.EarthquakeWarningRepository;
import com.simon.progexam.repository.ReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// Ansvar: Opretter jordskælvsvarsler og kobler sensormålinger til dem
@Service
@RequiredArgsConstructor
public class WarningService {

    private final EarthquakeWarningRepository warningRepository;
    private final ReadingRepository readingRepository;
    private final EpicenterCalculator epicenterCalculator;

    public void createWarning(List<Reading> readings) {
        EpicenterLocation epicenter = epicenterCalculator.calculate(readings);
        double magnitude = averageMagnitude(readings);

        EarthquakeWarning warning = buildWarning(epicenter, magnitude);
        EarthquakeWarning saved = warningRepository.save(warning);

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
}
