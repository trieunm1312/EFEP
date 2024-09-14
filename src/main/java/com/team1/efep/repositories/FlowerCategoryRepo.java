package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.FlowerCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowerCategoryRepo extends JpaRepository<FlowerCategory, Integer> {
}
