package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name);

    Optional<User> findByPhone(String phone);

}
