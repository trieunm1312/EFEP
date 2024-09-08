package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.FeedbackStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackStatusRepo extends JpaRepository<FeedbackStatus, Integer> {
}
