package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.DisableBusinessPlanRequest;
import com.team1.efep.repositories.BusinessPlanRepo;

import java.util.HashMap;
import java.util.Map;

public class DisableBusinessPlanValidation {
    public static Map<String, String> validate(DisableBusinessPlanRequest request, BusinessPlanRepo businessPlanRepo) {
        Map<String, String> error = new HashMap<>();
        //code validation here
         if(businessPlanRepo.existsById(request.getId())) {
             return MapConfig.buildMapKey(error,"Plan does not exist");
         }
        return error;
    }
}
