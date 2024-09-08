package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Flower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowerRepo extends JpaRepository<Flower, Integer> {
}
