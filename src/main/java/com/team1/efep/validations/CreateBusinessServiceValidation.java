package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.CreateBusinessServiceRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateBusinessServiceValidation {
    public static Map<String, String> validate(CreateBusinessServiceRequest request) {
        Map<String, String> error = new HashMap<>();
        //code validate here
        if(request.getName().isEmpty()){
            return MapConfig.buildMapKey(error, "Service name cannot be empty");
        }

        if(request.getDescription().isEmpty()){
            return MapConfig.buildMapKey(error, "Service description cannot be empty");
        }

        if(request.getPrice() <= 0) {
            return MapConfig.buildMapKey(error, "Service price must be greater than zero");
        }

        return error;
    }


}
