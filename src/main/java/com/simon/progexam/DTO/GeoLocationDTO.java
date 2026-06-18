package com.simon.progexam.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeoLocationDTO {

    @JsonProperty("display_name")
    private String name;

}
