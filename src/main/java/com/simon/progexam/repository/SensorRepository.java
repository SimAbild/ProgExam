package com.simon.progexam.repository;

import com.simon.progexam.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    Optional<Sensor> findBySensorId(String sensorId);
}
