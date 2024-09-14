package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepo extends JpaRepository<OrderStatus, Integer> {
}
