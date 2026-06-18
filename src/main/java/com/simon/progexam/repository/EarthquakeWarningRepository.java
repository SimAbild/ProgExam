package com.simon.progexam.repository;

import com.simon.progexam.entity.EarthquakeWarning;
import com.simon.progexam.entity.EarthquakeWarningStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EarthquakeWarningRepository extends JpaRepository<EarthquakeWarning, Integer> {


    List<EarthquakeWarning> findByStatus(EarthquakeWarningStatus status);
}
