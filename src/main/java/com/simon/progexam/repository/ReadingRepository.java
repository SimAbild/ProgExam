package com.simon.progexam.repository;

import com.simon.progexam.entity.Reading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingRepository extends JpaRepository<Reading, Integer> {
}
