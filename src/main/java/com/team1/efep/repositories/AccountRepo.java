package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Integer> {
}
