package com.team1.efep.repositories;

import com.team1.efep.models.entity_models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Integer> {
}
