package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.ViewProfileRequest;
import com.team1.efep.repositories.AccountRepo;

import java.util.HashMap;
import java.util.Map;

public class ViewProfileValidation {
    public static Map<String, String> validate(ViewProfileRequest request, AccountRepo accountRepo) {
        Map<String, String> error = new HashMap<>();
        //code validate here

        //Check user
        if (!accountRepo.existsById(request.getId())) {
            return MapConfig.buildMapKey(error, "Account does not exist");
        }
        return error;
    }
}

