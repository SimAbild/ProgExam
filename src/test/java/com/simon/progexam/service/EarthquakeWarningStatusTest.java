package com.simon.progexam.service;

import com.simon.progexam.entity.EarthquakeWarning;
import com.simon.progexam.entity.EarthquakeWarningStatus;
import com.simon.progexam.repository.EarthquakeWarningRepository;
import com.simon.progexam.repository.ReadingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EarthquakeWarningStatusTest {

    @Mock
    private EarthquakeWarningRepository warningRepository;

    @Mock
    private ReadingRepository readingRepository;

    @Mock
    private GeoLocator geoLocator;

    @Mock
    private EpicenterCalculator epicenterCalculator;

    @InjectMocks
    private EarthquakeWarningService earthquakeWarningService;

    private EarthquakeWarning buildWarning(EarthquakeWarningStatus status) {
        EarthquakeWarning warning = new EarthquakeWarning();
        warning.setStatus(status);
        return warning;
    }

    @Test
    void changeStatus_fromUnderReview_toActive_succeeds() {
        EarthquakeWarning warning = buildWarning(EarthquakeWarningStatus.UNDER_REVIEW);
        when(warningRepository.findById(1)).thenReturn(Optional.of(warning));
        when(warningRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        EarthquakeWarning result = earthquakeWarningService.changeEarthquakeWarningStatus(1, EarthquakeWarningStatus.ACTIVE);

        assertEquals(EarthquakeWarningStatus.ACTIVE, result.getStatus());
    }

    @Test
    void changeStatus_fromUnderReview_toFalseAlarm_succeeds() {
        EarthquakeWarning warning = buildWarning(EarthquakeWarningStatus.UNDER_REVIEW);
        when(warningRepository.findById(1)).thenReturn(Optional.of(warning));
        when(warningRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        EarthquakeWarning result = earthquakeWarningService.changeEarthquakeWarningStatus(1, EarthquakeWarningStatus.FALSE_ALARM);

        assertEquals(EarthquakeWarningStatus.FALSE_ALARM, result.getStatus());
    }

    @Test
    void changeStatus_fromActive_toNotActive_succeeds() {
        EarthquakeWarning warning = buildWarning(EarthquakeWarningStatus.ACTIVE);
        when(warningRepository.findById(1)).thenReturn(Optional.of(warning));
        when(warningRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        EarthquakeWarning result = earthquakeWarningService.changeEarthquakeWarningStatus(1, EarthquakeWarningStatus.NOT_ACTIVE);

        assertEquals(EarthquakeWarningStatus.NOT_ACTIVE, result.getStatus());
    }

    @Test
    void changeStatus_fromUnderReview_toNotActive_throwsException() {
        EarthquakeWarning warning = buildWarning(EarthquakeWarningStatus.UNDER_REVIEW);
        when(warningRepository.findById(1)).thenReturn(Optional.of(warning));

        assertThrows(IllegalArgumentException.class, () ->
                earthquakeWarningService.changeEarthquakeWarningStatus(1, EarthquakeWarningStatus.NOT_ACTIVE)
        );
    }

    @Test
    void changeStatus_fromFalseAlarm_throwsException() {
        EarthquakeWarning warning = buildWarning(EarthquakeWarningStatus.FALSE_ALARM);
        when(warningRepository.findById(1)).thenReturn(Optional.of(warning));

        assertThrows(IllegalArgumentException.class, () ->
                earthquakeWarningService.changeEarthquakeWarningStatus(1, EarthquakeWarningStatus.ACTIVE)
        );
    }

    @Test
    void changeStatus_fromNotActive_throwsException() {
        EarthquakeWarning warning = buildWarning(EarthquakeWarningStatus.NOT_ACTIVE);
        when(warningRepository.findById(1)).thenReturn(Optional.of(warning));

        assertThrows(IllegalArgumentException.class, () ->
                earthquakeWarningService.changeEarthquakeWarningStatus(1, EarthquakeWarningStatus.ACTIVE)
        );
    }
}
