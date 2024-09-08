package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.BusinessService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessServiceRepo extends JpaRepository<BusinessService, Integer> {
}
