package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.UpdateFlowerRequest;
import com.team1.efep.repositories.FlowerRepo;

import java.util.HashMap;
import java.util.Map;

public class UpdateFlowerValidation {

    public static Map<String, String> validate(UpdateFlowerRequest request, FlowerRepo flowerRepo) {
        Map<String, String> errors = new HashMap<>();
        if (request.getFlowerId() <= 0) {
            return MapConfig.buildMapKey(errors, "Invalid flower ID");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return MapConfig.buildMapKey(errors, "Flower name is required");
        } else if (request.getName().length() < 3 || request.getName().length() > 30) {
            return MapConfig.buildMapKey(errors, "Flower name must be between 3 and 30 characters");
        } else if (!request.getName().matches("^[a-zA-Z0-9 ]*$")) {
            return MapConfig.buildMapKey(errors, "Flower name must not contain special characters");
        } else if (flowerRepo.findByName(request.getName()).isPresent()) {
            return MapConfig.buildMapKey(errors, "Flower name already exists");
        }

        if (request.getPrice() <= 0) {
            return MapConfig.buildMapKey(errors, "Price must be greater than 0");
        }

        if (request.getFlowerAmount() <= 0) {
            return MapConfig.buildMapKey(errors, "Flower amount cannot be less than 1");
        }


        if (request.getQuantity() < 0) {
            return MapConfig.buildMapKey(errors, "Quantity must be greater than or equal to 0");
        }

//        if (request.getQuantity() > ) {
//            return MapConfig.buildMapKey(errors, "Quantity must be greater than or equal to 0");
//        }

        return errors;
    }
}
