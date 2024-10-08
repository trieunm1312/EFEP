package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.RegisterRequest;
import com.team1.efep.repositories.AccountRepo;

import java.util.HashMap;
import java.util.Map;

public class RegisterValidation {
    public static Map<String, String> validate(RegisterRequest request) {
        Map<String, String> errors = new HashMap<>();
        //code validate here

        // name isn't duplicated
        // --> nam is duplicated
//        if(request.getName() != null) {
//            MapConfig.buildMapKey(errors, "Name is duplicated");
//        }
//
//        // phone must be 10-digit number, isn't duplicated
//        // --> phone must not 10-digit number, is duplicated
//        if(request.getPhone().isBlank()) {
//            MapConfig.buildMapKey(errors, "Phone is duplicated");
//        }
//
//        // email invalid format email
//        // email invalid format email
//
//
//        //password is duplicated confirm password
//        //password isn't duplicated cofirm password
//        if(!request.getPassword().equals(request.getConfirmPassword())) {
//            MapConfig.buildMapKey(errors, "Password isn't matched");
//        }


        return errors;
    }
}