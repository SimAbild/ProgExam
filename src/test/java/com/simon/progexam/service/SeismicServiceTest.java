package com.simon.progexam.service;

import com.simon.progexam.DTO.SensorLocationDTO;
import com.simon.progexam.DTO.SensorReadingDTO;
import com.simon.progexam.entity.Reading;
import com.simon.progexam.entity.Sensor;
import com.simon.progexam.repository.ReadingRepository;
import com.simon.progexam.repository.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeismicServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private ReadingRepository readingRepository;

    @Mock
    private WarningService warningService;

    @InjectMocks
    private SeismicService seismicService;

    private SensorReadingDTO buildDto(String sensorId) {
        SensorLocationDTO location = new SensorLocationDTO();
        location.setLatitude(55.0);
        location.setLongitude(12.0);

        SensorReadingDTO dto = new SensorReadingDTO();
        dto.setSensorId(sensorId);
        dto.setSensorLocation(location);
        dto.setEstimatedDistanceToEpicenterKm(100.0);
        dto.setEstimatedMagnitude(4.5);
        return dto;
    }

    @BeforeEach
    void setUp() {
        when(sensorRepository.save(any(Sensor.class))).thenAnswer(i -> i.getArgument(0));
        when(readingRepository.save(any(Reading.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void processReadings_savesNewSensor_whenSensorNotFound() {
        when(sensorRepository.findBySensorId("s1")).thenReturn(Optional.empty());

        seismicService.processReadings(List.of(buildDto("s1")));

        verify(sensorRepository).save(any(Sensor.class));
    }

    @Test
    void processReadings_reusesExistingSensor_whenSensorFound() {
        Sensor existing = new Sensor();
        existing.setSensorId("s1");
        when(sensorRepository.findBySensorId("s1")).thenReturn(Optional.of(existing));

        seismicService.processReadings(List.of(buildDto("s1")));

        verify(sensorRepository, never()).save(any(Sensor.class));
    }

    @Test
    void processReadings_savesReading_forEachDto() {
        when(sensorRepository.findBySensorId(any())).thenReturn(Optional.empty());

        seismicService.processReadings(List.of(buildDto("s1"), buildDto("s2")));

        verify(readingRepository, times(2)).save(any(Reading.class));
    }

    @Test
    void processReadings_createsWarning_whenExactlyThreeReadings() {
        when(sensorRepository.findBySensorId(any())).thenReturn(Optional.empty());

        seismicService.processReadings(List.of(buildDto("s1"), buildDto("s2"), buildDto("s3")));

        verify(warningService).createWarning(any());
    }

    @Test
    void processReadings_doesNotCreateWarning_whenFewerThanThreeReadings() {
        when(sensorRepository.findBySensorId(any())).thenReturn(Optional.empty());

        seismicService.processReadings(List.of(buildDto("s1"), buildDto("s2")));

        verify(warningService, never()).createWarning(any());
    }
}
