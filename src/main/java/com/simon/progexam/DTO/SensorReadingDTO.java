package com.simon.progexam.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class SensorReadingDTO {
    String readingId;
    String sensorId;
    SensorLocationDTO sensorLocation;
    double estimatedDistanceToEpicenterKm;
    double estimatedMagnitude;
    LocalDateTime recordedAt;

}
