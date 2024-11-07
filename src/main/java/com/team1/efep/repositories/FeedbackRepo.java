package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Feedback;
import com.team1.efep.models.entity_models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepo extends JpaRepository<Feedback, Integer> {

    boolean existsByOrder_IdAndSeller_Id(int orderId, int sellerId);

}
