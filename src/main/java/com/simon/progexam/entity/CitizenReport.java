package com.simon.progexam.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity
public class CitizenReport {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    private double intensity;

    @ManyToOne
    @JoinColumn(name = "earthquake_warning_id")
    private EarthquakeWarning earthquakeWarning;

}
