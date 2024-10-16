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

        if(request.getPhone().isEmpty()) {
            return MapConfig.buildMapKey(error, "Phone cannot be empty");
        }

        if (!request.getPhone().matches("\\d{10}")) {
            return MapConfig.buildMapKey(error, "Phone number must be 10 digits");
        }

        return error;
    }
}
