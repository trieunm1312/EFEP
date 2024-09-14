package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.PurchasedPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedPlanStatusRepo extends JpaRepository<PurchasedPlanStatus, Integer> {
}
