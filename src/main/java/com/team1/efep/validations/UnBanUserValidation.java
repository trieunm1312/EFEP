package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.User;
import com.team1.efep.models.request_models.UnBanUserRequest;
import com.team1.efep.repositories.UserRepo;

import java.util.HashMap;
import java.util.Map;

public class UnBanUserValidation {
    public static Map<String, String> validate(UnBanUserRequest request, UserRepo userRepo) {
        Map<String, String> error = new HashMap<>();
        // code validate here
        User user = userRepo.findById(request.getId()).orElse(null);
        if(user == null) {
           return MapConfig.buildMapKey(error, "User does not exist");
        }

        if(user.getAccount().getStatus().equals(Status.ACCOUNT_STATUS_ACTIVE)) {
            return MapConfig.buildMapKey(error, "Account is active");
        }
        return error;
    }
}
