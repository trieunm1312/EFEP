package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.CreateBusinessPlanRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateBusinessPlanValidation {
    public static Map<String, String> validate(CreateBusinessPlanRequest request) {
        Map<String, String> error = new HashMap<>();
        //validation code here
        if(request.getName().isEmpty()){
           return MapConfig.buildMapKey(error, "Plan name cannot be empty");
        }

        if(request.getDescription().isEmpty()){
            return MapConfig.buildMapKey(error, "Plan description cannot be empty");
        }

        if(request.getPrice() <= 0) {
            return MapConfig.buildMapKey(error, "Plan price must be greater than zero");
        }

        if(request.getDuration() <= 0) {
            return MapConfig.buildMapKey(error, "Plan duration must be greater than zero");
        }

        return error;
    }
}
