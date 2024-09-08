package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Integer> {
}
