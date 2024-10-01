package com.team1.efep.validations;

import com.team1.efep.models.request_models.ChangePasswordRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordValidation {
    public static Map<String, String> validate(ChangePasswordRequest request) {
        Map<String, String> errors = new HashMap<String, String>();
        if(!request.getNewPassword().equals(request.getOldPassword())) {
            errors.put("error", "Passwords do not match");
        }
        // code validate here
        return errors;
    }
}
