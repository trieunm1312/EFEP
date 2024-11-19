package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.UserSellerApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSellerApplicationRepo extends JpaRepository<UserSellerApplication, Integer> {

    List<UserSellerApplication> findAllByUser_Id(Integer userId);
}
