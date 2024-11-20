package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.SellerApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerApplicationRepo extends JpaRepository<SellerApplication, Integer> {

}
