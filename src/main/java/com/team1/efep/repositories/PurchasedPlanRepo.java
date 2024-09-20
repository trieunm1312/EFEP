package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.PurchasedPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedPlanRepo extends JpaRepository<PurchasedPlan, Integer> {
}
