package com.simon.progexam.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Data
public class ReadingResponseDTO {
    private Integer id;

    private String sensorId;

    private double estimatedDistancetoEpicenterKm;

    private double estimatedMagnitude;

    private LocalDateTime recordedAt;
}
