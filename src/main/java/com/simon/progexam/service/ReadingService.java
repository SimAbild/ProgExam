package com.simon.progexam.service;

import com.simon.progexam.DTO.ReadingResponseDTO;
import com.simon.progexam.entity.EarthquakeWarning;
import com.simon.progexam.entity.Reading;
import com.simon.progexam.repository.ReadingRepository;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingService {

    private final ReadingRepository readingRepository;

    public List<ReadingResponseDTO> findReadings(){
        List<Reading> readings = readingRepository.findAll();
        List<ReadingResponseDTO> readingResponses = new ArrayList<>();

        for (Reading reading : readings){
            ReadingResponseDTO readingResponseDTO = new ReadingResponseDTO();
            readingResponseDTO.setId(reading.getId());
            readingResponseDTO.setSensorId(reading.getSensor().getSensorId());
            readingResponseDTO.setEstimatedDistancetoEpicenterKm(reading.getEstimatedDistanceToEpicenterKm());
            readingResponseDTO.setEstimatedMagnitude(reading.getEstimatedMagnitude());
            readingResponseDTO.setRecordedAt(reading.getRecordedAt());

           readingResponses.add(readingResponseDTO);
        }
        return readingResponses;

    }

    public List<Reading> findAllReadingsBySpecificEarthquakeWarning(EarthquakeWarning earthquakeWarning){
        List<Reading> readings = readingRepository.findByEarthquakeWarning(earthquakeWarning);
        return readings;
    }


}
