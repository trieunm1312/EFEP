package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.LoginRequest;
import com.team1.efep.repositories.AccountRepo;

import java.util.HashMap;
import java.util.Map;

public class LoginValidation {
    public static Map<String, String> validate(LoginRequest request, AccountRepo accountRepo) {
        Map<String, String> errors = new HashMap<>();
        // code validate here

        // check email (exist DB) and check pass  (exist DB)
        // --> check email and check pass not exits Database
        if (accountRepo.findByEmailAndPassword(request.getEmail(), request.getPassword()).orElse(null) == null) {
            MapConfig.buildMapKey(errors, "Email or password is incorrect");
        }

        return errors;
    }
}
