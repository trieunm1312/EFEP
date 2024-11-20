package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Feedback;
import com.team1.efep.models.entity_models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepo extends JpaRepository<Feedback, Integer> {

    boolean existsByOrder_IdAndSeller_Id(int orderId, int sellerId);

    List<Feedback> findAllBySeller_Id(int sellerId);

}
