package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.UpdateProfileRequest;
import com.team1.efep.repositories.AccountRepo;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileValidation {

    public static Map<String, String> validate(UpdateProfileRequest request, AccountRepo accountRepo) {
        Map<String, String> error = new HashMap<>();
        //code validate here

        if(!accountRepo.existsById(request.getId())){
            return MapConfig.buildMapKey(error, "Account does not exist");
        }

        if(request.getName().isEmpty()) {
            return MapConfig.buildMapKey(error, "Name cannot be empty");
        }

        if (!request.getName().matches("(?i)^[\\p{L}\\s]{1,20}( [\\p{L}\\s]{1,20})*$")) {
            return MapConfig.buildMapKey(error, "Name must contain only letters (including Vietnamese characters) and have a maximum length of 16 characters");
        }

        if(request.getPhone().isEmpty()) {
            return MapConfig.buildMapKey(error, "Phone cannot be empty");
        }

        if (!request.getPhone().matches("^(09|07|03)\\d{8}$")) {
            return MapConfig.buildMapKey(error, "Phone number must start with 09, 07, or 03 and be 10 digits long");
        }

        if(request.getAddress().isEmpty()) {
            return MapConfig.buildMapKey(error, "Address cannot be empty");
        }

        return error;
    }
}
