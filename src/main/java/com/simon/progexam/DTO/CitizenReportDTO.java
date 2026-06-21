package com.simon.progexam.DTO;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Data
public class CitizenReportDTO {

    @Positive
    private double intensity;
}
