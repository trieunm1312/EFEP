package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.repositories.UserRepo;

import java.util.HashMap;
import java.util.Map;

public class ViewBuyerListValidation {
    public static Map<String, String> validate(int sellerId, UserRepo userRepo) {
        Map<String, String> error = new HashMap<>();
        // code validate here
        if(!userRepo.existsById(sellerId)) {
            MapConfig.buildMapKey(error, "Seller does not exist");
        }
        return error;
    }
}
