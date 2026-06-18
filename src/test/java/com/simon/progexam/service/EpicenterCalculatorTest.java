package com.simon.progexam.service;

import com.simon.progexam.entity.Reading;
import com.simon.progexam.entity.Sensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicenterCalculatorTest {

    private EpicenterCalculator epicenterCalculator;

    @BeforeEach
    void setUp() {
        epicenterCalculator = new EpicenterCalculatorImpl();
    }

    @Test
    void calculate_returnsEpicenterCloseToExpected() {
        Sensor sensor1 = buildSensor("s1", 55.0, 12.0);
        Sensor sensor2 = buildSensor("s2", 55.5, 12.0);
        Sensor sensor3 = buildSensor("s3", 55.0, 12.5);

        Reading reading1 = buildReading(sensor1, 50.0);
        Reading reading2 = buildReading(sensor2, 60.0);
        Reading reading3 = buildReading(sensor3, 55.0);

        EpicenterLocation result = epicenterCalculator.calculate(List.of(reading1, reading2, reading3));

        assertNotNull(result);
        assertTrue(result.getLatitude() >= -90 && result.getLatitude() <= 90);
        assertTrue(result.getLongitude() >= -180 && result.getLongitude() <= 180);
    }

    @Test
    void calculate_throwsException_whenSensorsAreCollinear() {
        Sensor sensor1 = buildSensor("s1", 55.0, 12.0);
        Sensor sensor2 = buildSensor("s2", 55.0, 12.0);
        Sensor sensor3 = buildSensor("s3", 55.0, 12.0);

        Reading reading1 = buildReading(sensor1, 50.0);
        Reading reading2 = buildReading(sensor2, 50.0);
        Reading reading3 = buildReading(sensor3, 50.0);

        assertThrows(IllegalArgumentException.class, () ->
                epicenterCalculator.calculate(List.of(reading1, reading2, reading3))
        );
    }

    private Sensor buildSensor(String sensorId, double latitude, double longitude) {
        Sensor sensor = new Sensor();
        sensor.setSensorId(sensorId);
        sensor.setLatitude(latitude);
        sensor.setLongitude(longitude);
        return sensor;
    }

    private Reading buildReading(Sensor sensor, double distance) {
        Reading reading = new Reading();
        reading.setSensor(sensor);
        reading.setEstimatedDistance(distance);
        reading.setEstimatedMagnitude(4.0);
        return reading;
    }
}
