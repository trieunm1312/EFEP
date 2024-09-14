package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.BusinessPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessPlanRepo extends JpaRepository<BusinessPlan, Integer> {
}
