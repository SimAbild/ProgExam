package com.simon.progexam.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
public class CitizenReport {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    private double intensity;

    private String username;

    @ManyToOne
    @JoinColumn(name = "earthquake_warning_id")
    private EarthquakeWarning earthquakeWarning;

}
