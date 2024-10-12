package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.User;
import com.team1.efep.models.request_models.BanUserRequest;
import com.team1.efep.repositories.UserRepo;

import java.util.HashMap;
import java.util.Map;

public class BanUserValidation {
    public static Map<String, String> validate(BanUserRequest request, UserRepo userRepo) {
        Map<String, String> error = new HashMap<>();
       // code validate here
        User user = userRepo.findById(request.getId()).orElse(null);
        if(user == null) {
           return MapConfig.buildMapKey(error, "User does not exit");
        }

        if(user.getAccount().getStatus().equals(Status.ACCOUNT_STATUS_BANNED)) {
           return MapConfig.buildMapKey(error, "Account is Banned");
        }

        return error;
    }
}
