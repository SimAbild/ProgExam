package com.simon.progexam.service;

import com.simon.progexam.DTO.SensorReadingDTO;
import com.simon.progexam.entity.*;
import com.simon.progexam.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeismicService {

    private final SensorRepository sensorRepository;
    private final ReadingRepository readingRepository;
    private final EarthquakeWarningRepository warningRepository;

    public void processReadings(List<SensorReadingDTO> dtos) {
        List<Reading> savedReadings = new ArrayList<>();

        for (SensorReadingDTO dto : dtos) {
            Sensor sensor = sensorRepository.findBySensorId(dto.getSensorId())
                    .orElseGet(() -> {
                        Sensor s = new Sensor();
                        s.setSensorId(dto.getSensorId());
                        s.setLatitude(dto.getSensorLocation().getLatitude());
                        s.setLongitude(dto.getSensorLocation().getLongitude());
                        return sensorRepository.save(s);
                    });

            Reading reading = new Reading();
            reading.setSensor(sensor);
            reading.setEstimatedDistance(dto.getEstimatedDistanceToEpicenterKm());
            reading.setEstimatedMagnitude(dto.getEstimatedMagnitude());
            reading.setRecordedAt(dto.getRecordedAt() != null ? dto.getRecordedAt() : LocalDateTime.now());

            savedReadings.add(readingRepository.save(reading));
        }

        if (dtos.size() == 3) {
            createWarning(savedReadings);
        }
    }

    private void createWarning(List<Reading> readings) {
        double[] epicenter = calculateEpicenter(readings);
        double magnitude = readings.stream()
                .mapToDouble(Reading::getEstimatedMagnitude)
                .average()
                .orElse(0.0);

        EarthquakeWarning warning = new EarthquakeWarning();
        warning.setStatus(EarthquakeWarningStatus.UNDER_REVIEW);
        warning.setEpicenterLat(epicenter[0]);
        warning.setEpicenterLon(epicenter[1]);
        warning.setMagnitude(magnitude);
        warning.setCreatedAt(LocalDateTime.now());
        EarthquakeWarning saved = warningRepository.save(warning);

        for (Reading r : readings) {
            r.setWarning(saved);
            readingRepository.save(r);
        }
    }

    private double[] calculateEpicenter(List<Reading> readings) {
        // Weighted average: sensors closer to epicenter (smaller distance) weigh more
        double totalWeight = 0;
        double weightedLat = 0;
        double weightedLon = 0;

        for (Reading r : readings) {
            double distance = r.getEstimatedDistance();
            double weight = (distance > 0) ? 1.0 / distance : 1.0;
            weightedLat += r.getSensor().getLatitude() * weight;
            weightedLon += r.getSensor().getLongitude() * weight;
            totalWeight += weight;
        }

        return new double[]{weightedLat / totalWeight, weightedLon / totalWeight};
    }
}
