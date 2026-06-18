package com.simon.progexam.service;

import com.simon.progexam.DTO.SensorReadingDTO;
import com.simon.progexam.entity.*;
import com.simon.progexam.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Ansvar: Modtager og behandler indkommende sensordata fra Docker-containeren
@Service
@RequiredArgsConstructor
public class SeismicService {

    private final SensorRepository sensorRepository;
    private final ReadingRepository readingRepository;
    private final WarningService warningService;

    public void processReadings(List<SensorReadingDTO> dtos) {
        List<Reading> savedReadings = new ArrayList<>();

        for (SensorReadingDTO dto : dtos) {
            Sensor sensor = findOrCreateSensor(dto);
            savedReadings.add(readingRepository.save(buildReading(dto, sensor)));
        }

        if (dtos.size() == 3) {
            warningService.createWarning(savedReadings);
        }
    }

    private Sensor findOrCreateSensor(SensorReadingDTO dto) {
        return sensorRepository.findBySensorId(dto.getSensorId())
                .orElseGet(() -> sensorRepository.save(buildSensor(dto)));
    }

    private Sensor buildSensor(SensorReadingDTO dto) {
        Sensor sensor = new Sensor();
        sensor.setSensorId(dto.getSensorId());
        sensor.setLatitude(dto.getSensorLocation().getLatitude());
        sensor.setLongitude(dto.getSensorLocation().getLongitude());
        return sensor;
    }

    private Reading buildReading(SensorReadingDTO dto, Sensor sensor) {
        Reading reading = new Reading();
        reading.setSensor(sensor);
        reading.setEstimatedDistanceToEpicenterKm(dto.getEstimatedDistanceToEpicenterKm());
        reading.setEstimatedMagnitude(dto.getEstimatedMagnitude());
        reading.setRecordedAt(dto.getRecordedAt() != null ? dto.getRecordedAt() : LocalDateTime.now());
        return reading;
    }
}
