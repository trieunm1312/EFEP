package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.ChangePasswordRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordValidation {
    public static Map<String, String> validate(ChangePasswordRequest request) {
        Map<String, String> error = new HashMap<String, String>();


        // 1. Current password validation
        if (request.getCurrentPassword().isEmpty()) {
            return MapConfig.buildMapKey(error, "Current password cannot be empty");
        }

        // 2. New password validation (not empty)
        if (request.getNewPassword().isEmpty()) {
            return MapConfig.buildMapKey(error, "New password cannot be empty");
        }

        // 3. Check new password format
        if (!request.getNewPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            return MapConfig.buildMapKey(error, "New password must contain at least 8 characters, one uppercase letter, one lowercase letter, one digit, and one special character");
        }

        // 4. New password must be different from current password
        if (request.getNewPassword().equals(request.getCurrentPassword())) {
            return MapConfig.buildMapKey(error, "New password must be different from the current password");
        }

        // 5. Confirm password matches new password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return MapConfig.buildMapKey(error, "New password and confirm password do not match");
        }


        return error;
    }
}