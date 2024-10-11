package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.request_models.RenewPasswordRequest;
import com.team1.efep.repositories.AccountRepo;

import java.util.HashMap;
import java.util.Map;

public class RenewPasswordValidation {
    public static Map<String, String> validate(RenewPasswordRequest request, AccountRepo accountRepo) {
        Map<String, String> error = new HashMap<>();
        // code validate here
        // email invalid format email
        if (request.getEmail().isEmpty()) {
            return MapConfig.buildMapKey(error, "Email cannot be empty");
        }

        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return MapConfig.buildMapKey(error, "Email is invalid format");
        }

        Account acc = accountRepo.findByEmail(request.getEmail()).orElse(null);

        if (acc == null) {
            return MapConfig.buildMapKey(error, "Account with this email does not exist");
        }

        //password is equal confirmed password
        //password isn't equal confirmed password
        if (request.getPassword().isEmpty()) {
            return MapConfig.buildMapKey(error, "Password cannot be empty");

        }

        //Minimum eight characters, at least one letter, one number and one special character:
        if (!request.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            return MapConfig.buildMapKey(error, "Password is invalid format");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return MapConfig.buildMapKey(error, "Confirmed password doesn't match the password");

        }

        if(acc.getPassword().equals(request.getPassword())) {
            return MapConfig.buildMapKey(error, "New password cannot be the same as the current password");
        }

        return error;
    }
}
