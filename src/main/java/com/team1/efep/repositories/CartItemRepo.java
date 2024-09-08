package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {
}
