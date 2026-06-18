package com.simon.progexam.repository;

import com.simon.progexam.entity.EarthquakeWarning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EarthquakeWarningRepository extends JpaRepository<EarthquakeWarning, Integer> {
}
