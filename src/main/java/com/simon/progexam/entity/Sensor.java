package com.simon.progexam.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String sensorId;

    private double latitude;

    private double longitude;
}
