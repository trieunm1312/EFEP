package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.UpdateBusinessServiceRequest;
import com.team1.efep.repositories.BusinessServiceRepo;

import java.util.HashMap;
import java.util.Map;

public class UpdateBusinessServiceValidation {

    private static final int MAX_NAME_LENGTH = 20; // Define maximum length for name
    private static final int MAX_DESCRIPTION_LENGTH = 500; // Define maximum length for description

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

        if (request.getName().length() > MAX_NAME_LENGTH) {
            return MapConfig.buildMapKey(error, "Name cannot exceed " + MAX_NAME_LENGTH + " characters.");
        }

        // Validate the price
        if (request.getPrice() <= 0) {
            return MapConfig.buildMapKey(error, "Price must be provided and should not be negative.");
        }

        // Validate the description
        if (request.getDescription().isEmpty()) {
            return MapConfig.buildMapKey(error, "Description is required.");
        }

        if (request.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            return MapConfig.buildMapKey(error, "Description cannot exceed " + MAX_DESCRIPTION_LENGTH + " characters.");

        }
        return error;
    }
}
