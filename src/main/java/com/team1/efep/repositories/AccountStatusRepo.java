package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountStatusRepo extends JpaRepository<AccountStatus, Integer> {
}
