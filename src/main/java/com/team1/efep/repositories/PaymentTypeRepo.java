package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTypeRepo extends JpaRepository<PaymentType, Integer> {
}
