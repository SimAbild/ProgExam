package com.simon.progexam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Entity
public class EarthquakeWarning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private EarthquakeWarningStatus status;

    private double epicenterLat;

    private double epicenterLon;

    private double magnitude;

    private LocalDateTime createdAt;

    private String geoLocationName;
}
