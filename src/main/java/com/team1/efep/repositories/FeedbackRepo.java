package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepo extends JpaRepository<Feedback, Integer> {
}
