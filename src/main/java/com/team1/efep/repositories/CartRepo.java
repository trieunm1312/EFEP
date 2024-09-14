package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser_Account_Id(int id);
}
