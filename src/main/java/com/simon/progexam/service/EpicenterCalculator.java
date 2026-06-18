package com.simon.progexam.service;

import com.simon.progexam.entity.Reading;

import java.util.List;

public interface EpicenterCalculator {
    EpicenterLocation calculate(List<Reading> readings);
}
