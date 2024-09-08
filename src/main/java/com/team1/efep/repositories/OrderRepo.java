package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Integer> {
}
