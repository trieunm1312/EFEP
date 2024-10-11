package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.request_models.ForgotPasswordRequest;
import com.team1.efep.repositories.AccountRepo;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordValidation {
    public static Map<String, String> validate(ForgotPasswordRequest request, AccountRepo acc) {
        Map<String, String> error = new HashMap<>();
        // code validate here
        Account account = acc.findByEmail(request.getToEmail()).orElse(null);
        if(account == null) {
            return MapConfig.buildMapKey(error, "Account with this email does not exist");
        }
        return error;
    }
}
