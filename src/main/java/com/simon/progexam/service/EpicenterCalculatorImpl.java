package com.simon.progexam.service;

import com.simon.progexam.entity.Reading;
import com.simon.progexam.entity.Sensor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EpicenterCalculatorImpl implements EpicenterCalculator {

    @Override
    public EpicenterLocation calculate(List<Reading> readings) {
        if (readings.size() < 3) {
            throw new IllegalArgumentException("Der skal være mindst tre målinger.");
        }

        Sensor ref = readings.getFirst().getSensor();

        double refLatRad = Math.toRadians(ref.getLatitude());
        double refLonRad = Math.toRadians(ref.getLongitude());

        double earthRadius = 6371.00;

        double[] x = new double[3];
        double[] y = new double[3];
        double[] d = new double[3];

        for (int i = 0; i < 3; i++) {
            Sensor loc = readings.get(i).getSensor();

            double latRad = Math.toRadians(loc.getLatitude());
            double lonRad = Math.toRadians(loc.getLongitude());

            x[i] = earthRadius * (lonRad - refLonRad) * Math.cos(refLatRad);
            y[i] = earthRadius * (latRad - refLatRad);
            d[i] = readings.get(i).getEstimatedDistanceToEpicenterKm();
        }

        double A = 2 * (x[1] - x[0]);
        double B = 2 * (y[1] - y[0]);
        double C = d[0] * d[0] - d[1] * d[1] - x[0] * x[0] + x[1] * x[1] - y[0] * y[0] + y[1] * y[1];

        double D = 2 * (x[2] - x[1]);
        double E = 2 * (y[2] - y[1]);
        double F = d[1] * d[1] - d[2] * d[2] - x[1] * x[1] + x[2] * x[2] - y[1] * y[1] + y[2] * y[2];

        double denominator = A * E - B * D;

        if (Math.abs(denominator) < 1e-12) {
            throw new IllegalArgumentException("Målepunkterne giver ingen stabil løsning.");
        }

        double epicenterX = (C * E - B * F) / denominator;
        double epicenterY = (A * F - C * D) / denominator;

        double epicenterLatRad = refLatRad + epicenterY / earthRadius;
        double epicenterLonRad = refLonRad + epicenterX / (earthRadius * Math.cos(refLatRad));

        return new EpicenterLocation(Math.toDegrees(epicenterLatRad), Math.toDegrees(epicenterLonRad));
    }
}
