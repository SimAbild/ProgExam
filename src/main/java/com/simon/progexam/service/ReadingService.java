package com.simon.progexam.service;

import com.simon.progexam.DTO.ReadingResponseDTO;
import com.simon.progexam.entity.Reading;
import com.simon.progexam.repository.ReadingRepository;
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
        List<ReadingResponseDTO> ReadingResponseDtos = new ArrayList<>();

        //for loop, for alle readings i findall, loop over og map dem i en repsonseDTO

        for (Reading reading : readings){
            ReadingResponseDTO readingResponseDTO = new ReadingResponseDTO();
            readingResponseDTO.setId(reading.getId());
            readingResponseDTO.setSensorId(reading.getSensor().getSensorId());
            readingResponseDTO.setEstimatedDistancetoEpicenterKm(reading.getEstimatedDistanceToEpicenterKm());
            readingResponseDTO.setEstimatedMagnitude(reading.getEstimatedMagnitude());
            readingResponseDTO.setRecordedAt(reading.getRecordedAt());

           ReadingResponseDtos.add(readingResponseDTO);
        }

    }


}
