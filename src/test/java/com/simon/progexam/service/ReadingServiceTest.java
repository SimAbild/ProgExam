package com.simon.progexam.service;

import com.simon.progexam.DTO.ReadingResponseDTO;
import com.simon.progexam.entity.Reading;
import com.simon.progexam.entity.Sensor;
import com.simon.progexam.repository.ReadingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadingServiceTest {

    @Mock
    private ReadingRepository readingRepository;

    @InjectMocks
    private ReadingService readingService;

    private Reading buildReading(int id, String sensorId, double distance, double magnitude) {
        Sensor sensor = new Sensor();
        sensor.setSensorId(sensorId);

        Reading reading = new Reading();
        reading.setId(id);
        reading.setSensor(sensor);
        reading.setEstimatedDistanceToEpicenterKm(distance);
        reading.setEstimatedMagnitude(magnitude);
        reading.setRecordedAt(LocalDateTime.now());
        return reading;
    }

    @Test
    void findReadings_returnsEmptyList_whenNoReadingsExist() {
        when(readingRepository.findAll()).thenReturn(List.of());

        List<ReadingResponseDTO> result = readingService.findReadings();

        assertTrue(result.isEmpty());
    }

    @Test
    void findReadings_returnsMappedReadings() {
        when(readingRepository.findAll()).thenReturn(List.of(
                buildReading(1, "sensor-1", 100.0, 4.5),
                buildReading(2, "sensor-2", 200.0, 3.2)
        ));

        List<ReadingResponseDTO> result = readingService.findReadings();

        assertEquals(2, result.size());
    }

    @Test
    void findReadings_mapsFieldsCorrectly() {
        when(readingRepository.findAll()).thenReturn(List.of(
                buildReading(1, "sensor-1", 100.0, 4.5)
        ));

        ReadingResponseDTO result = readingService.findReadings().get(0);

        assertEquals(1, result.getId());
        assertEquals("sensor-1", result.getSensorId());
        assertEquals(100.0, result.getEstimatedDistancetoEpicenterKm());
        assertEquals(4.5, result.getEstimatedMagnitude());
        assertNotNull(result.getRecordedAt());
    }
}
