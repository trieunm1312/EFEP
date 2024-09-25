package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepo extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findAllByFlower_Seller_Id(int accountId);
}
