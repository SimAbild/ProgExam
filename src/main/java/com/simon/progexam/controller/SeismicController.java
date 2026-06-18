package com.simon.progexam.controller;

import com.simon.progexam.DTO.SensorReadingDTO;
import com.simon.progexam.service.SeismicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SeismicController {

    private final SeismicService seismicService;

    @PostMapping("/sensor-data")
    public ResponseEntity<String> receiveSensorData(@RequestBody List<SensorReadingDTO> readings) {
        seismicService.processReadings(readings);
        return ResponseEntity.ok("Readings processed");
    }

    @GetMapping("/sensor-data")
    public SensorReadingDTO
}
