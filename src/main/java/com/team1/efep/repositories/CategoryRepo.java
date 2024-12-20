package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
    Category findByName(String name);
}
