package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Flower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlowerRepo extends JpaRepository<Flower, Integer> {

    Optional<Flower> findByName(String name);

    List<Flower> findAllBySeller_Id(int id);

    List<Flower> findAllBySeller_IdAndName(int id, String name);

    List<Flower> findByStatus(String status);

    List<Flower> findAllBySeller_User_Account_Id(int id);
}
