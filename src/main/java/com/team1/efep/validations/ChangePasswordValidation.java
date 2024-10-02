package com.team1.efep.validations;

import com.team1.efep.models.request_models.ChangePasswordRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordValidation {
    public static Map<String, String> validate(ChangePasswordRequest request) {
        Map<String, String> errors = new HashMap<String, String>();

        if(request.getCurrentPassword().equals(request.getCurrentPassword())) {
            errors.put("error", "Current password does not match the current password.");
        }

        if(request.getNewPassword().equals(request.getCurrentPassword())) {
            errors.put("error", "New password must be different from the current password.");
        }

        if(request.getNewPassword().equals(request.getConfirmPassword())) {
            errors.put("error", "New password and confirm password do not match.");
        }
        // code validate here
        return errors;
    }
}