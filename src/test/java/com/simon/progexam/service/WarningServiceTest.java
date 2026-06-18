package com.simon.progexam.service;

import com.simon.progexam.entity.*;
import com.simon.progexam.repository.EarthquakeWarningRepository;
import com.simon.progexam.repository.ReadingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarningServiceTest {

    @Mock
    private EarthquakeWarningRepository warningRepository;

    @Mock
    private ReadingRepository readingRepository;

    @Mock
    private EpicenterCalculator epicenterCalculator;

    @InjectMocks
    private WarningService warningService;

    private Reading buildReading(double magnitude) {
        Sensor sensor = new Sensor();
        sensor.setSensorId("s1");

        Reading reading = new Reading();
        reading.setSensor(sensor);
        reading.setEstimatedMagnitude(magnitude);
        reading.setEstimatedDistanceToEpicenterKm(100.0);
        return reading;
    }

    @Test
    void createWarning_savesWarningWithStatusUnderReview() {
        when(epicenterCalculator.calculate(any())).thenReturn(new EpicenterLocation(55.0, 12.0));
        when(warningRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        warningService.createWarning(List.of(buildReading(4.0), buildReading(4.0), buildReading(4.0)));

        verify(warningRepository).save(argThat(warning ->
                warning.getStatus() == EarthquakeWarningStatus.UNDER_REVIEW
        ));
    }

    @Test
    void createWarning_linksAllReadingsToWarning() {
        when(epicenterCalculator.calculate(any())).thenReturn(new EpicenterLocation(55.0, 12.0));
        EarthquakeWarning savedWarning = new EarthquakeWarning();
        when(warningRepository.save(any())).thenReturn(savedWarning);

        warningService.createWarning(List.of(buildReading(4.0), buildReading(4.0), buildReading(4.0)));

        verify(readingRepository, times(3)).save(any(Reading.class));
    }

    @Test
    void createWarning_setsCorrectAverageMagnitude() {
        when(epicenterCalculator.calculate(any())).thenReturn(new EpicenterLocation(55.0, 12.0));
        when(warningRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        warningService.createWarning(List.of(buildReading(3.0), buildReading(4.0), buildReading(5.0)));

        verify(warningRepository).save(argThat(warning ->
                warning.getMagnitude() == 4.0
        ));
    }
}
