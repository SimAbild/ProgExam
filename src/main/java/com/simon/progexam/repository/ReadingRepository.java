package com.simon.progexam.repository;

import com.simon.progexam.entity.EarthquakeWarning;
import com.simon.progexam.entity.Reading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadingRepository extends JpaRepository<Reading, Integer> {

    List<Reading> findByEarthquakeWarning(EarthquakeWarning earthquakeWarning);

}
