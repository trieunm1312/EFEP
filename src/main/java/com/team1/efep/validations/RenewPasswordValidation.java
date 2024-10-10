package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.RenewPasswordRequest;
import com.team1.efep.repositories.AccountRepo;

import java.util.HashMap;
import java.util.Map;

public class RenewPasswordValidation {
    public static Map<String, String> validate(RenewPasswordRequest request, AccountRepo accountRepo) {
        Map<String, String> errors = new HashMap<>();
        // code validate here
        // email invalid format email
        if (request.getEmail().isEmpty()) {
            return MapConfig.buildMapKey(errors, "Email cannot be empty");
        }

        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return MapConfig.buildMapKey(errors, "Email is in invalid format");
        }

        if (accountRepo.findByEmail(request.getEmail()).isEmpty()) {
            return MapConfig.buildMapKey(errors, "Account with this email does not exist");
        }

        //password is equal confirmed password
        //password isn't equal confirmed password
        if (request.getPassword().isEmpty()) {
            return MapConfig.buildMapKey(errors, "Password cannot be empty");

        }

        //Minimum eight characters, at least one letter, one number and one special character:
        if (!request.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            return MapConfig.buildMapKey(errors, "Password is in invalid format");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return MapConfig.buildMapKey(errors, "Confirmed password doesn't match the password");

        }
        return errors;
    }
}
