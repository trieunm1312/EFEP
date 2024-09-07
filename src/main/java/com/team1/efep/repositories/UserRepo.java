package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
}
