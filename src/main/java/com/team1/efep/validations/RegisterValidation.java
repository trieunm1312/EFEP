package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.RegisterRequest;
import com.team1.efep.repositories.AccountRepo;

import java.util.HashMap;
import java.util.Map;

public class RegisterValidation {

    public static Map<String, String> validate(RegisterRequest request, AccountRepo accountRepo) {
        Map<String, String> errors = new HashMap<>();
        //code validate here

        // name isn't duplicated
        // --> name is duplicated
        if (request.getName().isEmpty()) {
            return MapConfig.buildMapKey(errors, "Name cannot be empty");
        }

        if (accountRepo.findByUser_Name(request.getName()).isPresent()) {
            return MapConfig.buildMapKey(errors, "Name already exists");
        }

        // phone must be 10-digit number, isn't duplicated
        // --> phone must not 10-digit number, is duplicated
        if (request.getPhone().isEmpty()) {
            return MapConfig.buildMapKey(errors, "Phone cannot be empty");
        }

        if (accountRepo.findByUser_Phone(request.getPhone()).isPresent()) {
            return MapConfig.buildMapKey(errors, "Phone already exists");
        }

        if (!request.getPhone().matches("\\d{10}")) {
            return MapConfig.buildMapKey(errors, "Phone number must be 10 digits");
        }

        // email invalid format email
        if (request.getEmail().isEmpty()) {
            return MapConfig.buildMapKey(errors, "Email cannot be empty");
        }

        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return MapConfig.buildMapKey(errors, "Email is in invalid format");
        }

        if (accountRepo.findByEmail(request.getEmail()).isPresent()) {
            return MapConfig.buildMapKey(errors, "Email already exists");
        }

        //password is equal confirmed password
        //password isn't equal confirmed password
        if (request.getPassword().isEmpty()) {
            return MapConfig.buildMapKey(errors, "Password cannot be empty");
        }

        if (!request.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            return MapConfig.buildMapKey(errors, "Password is in invalid format");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return MapConfig.buildMapKey(errors, "Confirmed password doesn't match the password");

        }

        return errors;
    }
}