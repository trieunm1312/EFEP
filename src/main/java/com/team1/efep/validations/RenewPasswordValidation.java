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

        // 1. Email validation (not empty)
        if (request.getEmail().isEmpty()) {
            return MapConfig.buildMapKey(error, "Email cannot be empty");
        }

        // 2. Improved email format validation
        if (!request.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return MapConfig.buildMapKey(error, "Email is in invalid format");
        }

        // 3. Check if account exists for the email
        Account acc = accountRepo.findByEmail(request.getEmail()).orElse(null);
        if (acc == null) {
            return MapConfig.buildMapKey(error, "Account with this email does not exist");
        }

        // 4. Password validation (not empty)
        if (request.getPassword().isEmpty()) {
            return MapConfig.buildMapKey(error, "Password cannot be empty");
        }

        // 5. Password format validation
        if (!request.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            return MapConfig.buildMapKey(error, "Password is in invalid format");
        }

        // 6. Check if new password and confirm password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return MapConfig.buildMapKey(error, "Confirmed password doesn't match the password");
        }

        // 7. Ensure new password is not the same as the current password
        if (acc.getPassword().equals(request.getPassword())) {
            return MapConfig.buildMapKey(error, "New password cannot be the same as the current password");
        }
        return error;
    }
}
