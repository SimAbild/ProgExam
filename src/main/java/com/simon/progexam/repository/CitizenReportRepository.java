package com.simon.progexam.repository;

import com.simon.progexam.entity.CitizenReport;
import com.simon.progexam.entity.EarthquakeWarning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitizenReportRepository extends JpaRepository<CitizenReport,Integer> {

    List<CitizenReport> findByEarthquakeWarning(EarthquakeWarning earthquakeWarning);
    Integer countByEarthquakeWarning(EarthquakeWarning earthquakeWarning);
    boolean existsByEarthquakeWarningAndUsername(EarthquakeWarning earthquakeWarning, String username);


}
