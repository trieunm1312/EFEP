package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.request_models.RenewPasswordRequest;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.utils.PasswordEncryptUtil;

import java.util.HashMap;
import java.util.Map;

public class RenewPasswordValidation {
    public static Map<String, String> validate(RenewPasswordRequest request, AccountRepo accountRepo) {
        Map<String, String> error = new HashMap<>();

        //Check if account exists for the email
        Account acc = accountRepo.findByEmail(request.getEmail()).orElse(null);
        if (acc == null) {
            return MapConfig.buildMapKey(error, "Account with this email does not exist");
        }

        //Password validation (not empty)
        if (request.getPassword().isEmpty()) {
            return MapConfig.buildMapKey(error, "Password cannot be empty");
        }

        //Password format validation
        if (!request.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")) {
            return MapConfig.buildMapKey(error, "Password is in invalid format");
        }

        //Ensure new password is not the same as the current password
        if (acc.getPassword().equals(PasswordEncryptUtil.encrypt(request.getPassword()))) {
            return MapConfig.buildMapKey(error, "New password cannot be the same as the current password");
        }

        //Check if new password and confirm password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return MapConfig.buildMapKey(error, "Confirmed password doesn't match the password");
        }


        return error;
    }
}
