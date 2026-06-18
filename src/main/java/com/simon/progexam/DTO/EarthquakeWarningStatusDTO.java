package com.simon.progexam.DTO;

import com.simon.progexam.entity.EarthquakeWarningStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Data
public class EarthquakeWarningStatusDTO {

    EarthquakeWarningStatus status;
}
