package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.ChangePasswordRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordValidation {
    public static Map<String, String> validate(ChangePasswordRequest request) {
        Map<String, String> errors = new HashMap<String, String>();

        if(request.getCurrentPassword().isEmpty()) {
            return MapConfig.buildMapKey(errors,"Current password cannot be empty");
        }

        if(request.getNewPassword().isEmpty()) {
            return MapConfig.buildMapKey(errors,"New password cannot be empty");
        }

        if(request.getNewPassword().equals(request.getCurrentPassword())) {
           return MapConfig.buildMapKey(errors,"New password must be different from the current password");
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            return MapConfig.buildMapKey(errors, "New password and confirm password do not match");
        }
        // code validate here
        return errors;
    }
}