package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Integer> {

    Optional<Account> findByEmailAndPassword(String email, String password);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByIdAndPassword(int id, String password);

    Optional<Account> findByUserName(String name);

    Optional<Account> findByUserPhone(String phone);

    Optional<Account> findByPassword(String password);
}
