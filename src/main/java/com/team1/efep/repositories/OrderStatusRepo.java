package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderStatusRepo extends JpaRepository<OrderStatus, Integer> {

    Optional<OrderStatus> findByStatus(String status);
}
