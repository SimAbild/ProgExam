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
        Sensor ref = readings.get(0).getSensor();
        double refLatRad = Math.toRadians(ref.getLatitude());
        double refLonRad = Math.toRadians(ref.getLongitude());
        double earthRadius = 6371.0;

        double[] x = new double[3];
        double[] y = new double[3];
        double[] d = new double[3];

        for (int i = 0; i < 3; i++) {
            Sensor s = readings.get(i).getSensor();
            double latRad = Math.toRadians(s.getLatitude());
            double lonRad = Math.toRadians(s.getLongitude());
            x[i] = earthRadius * (lonRad - refLonRad) * Math.cos(refLatRad);
            y[i] = earthRadius * (latRad - refLatRad);
            d[i] = readings.get(i).getEstimatedDistance();
        }

        double A = 2 * (x[1] - x[0]);
        double B = 2 * (y[1] - y[0]);
        double C = d[0]*d[0] - d[1]*d[1] - x[0]*x[0] + x[1]*x[1] - y[0]*y[0] + y[1]*y[1];

        double D = 2 * (x[2] - x[1]);
        double E = 2 * (y[2] - y[1]);
        double F = d[1]*d[1] - d[2]*d[2] - x[1]*x[1] + x[2]*x[2] - y[1]*y[1] + y[2]*y[2];

        double denominator = A * E - B * D;
        if (Math.abs(denominator) < 1e-12) {
            throw new IllegalArgumentException("Målepunkterne giver ingen stabil løsning.");
        }

        double epicenterX = (C * E - B * F) / denominator;
        double epicenterY = (A * F - C * D) / denominator;

        double epicenterLatRad = refLatRad + epicenterY / earthRadius;
        double epicenterLonRad = refLonRad + epicenterX / (earthRadius * Math.cos(refLatRad));

        return new double[]{Math.toDegrees(epicenterLatRad), Math.toDegrees(epicenterLonRad)};
    }
}
