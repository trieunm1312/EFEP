package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Flower;
import com.team1.efep.models.entity_models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Integer> {

    List<Order> findAllByUser_Id(int id);

    int countByStatus(String status);

}
