package com.simon.progexam.service;

import com.simon.progexam.entity.Reading;
import com.simon.progexam.entity.Sensor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EpicenterCalculatorImpl implements EpicenterCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    public EpicenterLocation calculate(List<Reading> readings) {
        Sensor ref = readings.get(0).getSensor();
        double refLatRad = Math.toRadians(ref.getLatitude());
        double refLonRad = Math.toRadians(ref.getLongitude());

        double[] x = new double[3];
        double[] y = new double[3];
        double[] d = new double[3];

        for (int i = 0; i < 3; i++) {
            Sensor s = readings.get(i).getSensor();
            double latRad = Math.toRadians(s.getLatitude());
            double lonRad = Math.toRadians(s.getLongitude());
            x[i] = EARTH_RADIUS_KM * (lonRad - refLonRad) * Math.cos(refLatRad);
            y[i] = EARTH_RADIUS_KM * (latRad - refLatRad);
            d[i] = readings.get(i).getEstimatedDistanceToEpicenterKm();
        }

        double[] epicenterXY = solveLinearSystem(x, y, d);

        double epicenterLatRad = refLatRad + epicenterXY[1] / EARTH_RADIUS_KM;
        double epicenterLonRad = refLonRad + epicenterXY[0] / (EARTH_RADIUS_KM * Math.cos(refLatRad));

        return new EpicenterLocation(Math.toDegrees(epicenterLatRad), Math.toDegrees(epicenterLonRad));
    }

    private double[] solveLinearSystem(double[] x, double[] y, double[] d) {
        double A = 2 * (x[1] - x[0]);
        double B = 2 * (y[1] - y[0]);
        double C = d[0]*d[0] - d[1]*d[1] - x[0]*x[0] + x[1]*x[1] - y[0]*y[0] + y[1]*y[1];

        double D = 2 * (x[2] - x[1]);
        double E = 2 * (y[2] - y[1]);
        double F = d[1]*d[1] - d[2]*d[2] - x[1]*x[1] + x[2]*x[2] - y[1]*y[1] + y[2]*y[2];

        double denominator = A * E - B * D;
        if (Math.abs(denominator) < 1e-12) {
            throw new IllegalArgumentException("Målepunkterne giver ingen stabil løsning.");
        }

        return new double[]{
                (C * E - B * F) / denominator,
                (A * F - C * D) / denominator
        };
    }
}
