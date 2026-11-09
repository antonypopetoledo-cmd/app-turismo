package com.example.demo.repositories;

import com.example.demo.models.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TouristRepository extends JpaRepository<Tourist, Long> {
    
}
