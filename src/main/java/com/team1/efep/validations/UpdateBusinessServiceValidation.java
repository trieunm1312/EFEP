package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.UpdateBusinessServiceRequest;
import com.team1.efep.repositories.BusinessServiceRepo;

import java.util.HashMap;
import java.util.Map;

public class UpdateBusinessServiceValidation {

    public static Map<String, String> validate(UpdateBusinessServiceRequest request, BusinessServiceRepo businessServiceRepo) {

        Map<String, String> error = new HashMap<>();
        //validation code here
        if(!businessServiceRepo.existsById(request.getId())) {
            return MapConfig.buildMapKey(error, "Business service does not exist");
        }

        // Validate the name
        if (request.getName().isEmpty()) {
            return MapConfig.buildMapKey(error, "Name is required.");
        }

        // Validate the price
        if (request.getPrice() <= 0) {
            return MapConfig.buildMapKey(error, "Price must be provided and should not be negative.");
        }

        // Validate the description
        if (request.getDescription().isEmpty()) {
            return MapConfig.buildMapKey(error, "Description is required.");
        }

        return error;
    }
}
