package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepo extends JpaRepository<Seller, Integer> {
}
