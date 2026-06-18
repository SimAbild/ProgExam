package com.simon.progexam.service;

import com.simon.progexam.DTO.GeoLocationDTO;
import com.simon.progexam.service.GeoLocator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class GeoLocatorImpl implements GeoLocator {

    private final WebClient webClient = WebClient.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String locate(double latitude, double longitude) {
        String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude;

        String json = webClient.get()
                .uri(url)
                .header("User-Agent", "SeismicMonitor/1.0")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            GeoLocationDTO geoLocationDTO = objectMapper.readValue(json, GeoLocationDTO.class);
            return geoLocationDTO.getName();
        } catch (Exception e) {
            throw new RuntimeException("Kunne ikke parse geolokation");
        }
    }
}
