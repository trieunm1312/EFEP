package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.FlowerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowerStatusRepo extends JpaRepository<FlowerStatus, Integer> {
    FlowerStatus findByStatus(String status);
}
