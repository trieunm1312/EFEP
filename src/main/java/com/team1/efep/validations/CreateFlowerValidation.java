package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.entity_models.Seller;
import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.repositories.FlowerRepo;

import java.util.HashMap;
import java.util.Map;

public class CreateFlowerValidation {

    public static Map<String, String> validateInput(CreateFlowerRequest request, FlowerRepo flowerRepo, Seller seller) {
        Map<String, String> error = new HashMap<>();
        if (request.getName().trim().isEmpty()) {
            return MapConfig.buildMapKey(error, "Flower name is required");
        } else if (request.getName().length() < 3 || request.getName().length() > 30) {
            return MapConfig.buildMapKey(error, "Flower name must be between 3 and 30 characters");
        } else if (flowerRepo.findByName(request.getName()).isPresent()) {
            return MapConfig.buildMapKey(error, "Flower name already exists");
        } else if (!request.getName().matches("^[a-zA-Z0-9 ]*$")) {
            return MapConfig.buildMapKey(error, "Flower name must not contain special characters");
        }

        if (request.getPrice() == null) {
            return MapConfig.buildMapKey(error, "Price is required");
        } else if (request.getPrice() <= 0) {
            return MapConfig.buildMapKey(error, "Price must be greater than 0");
        }

        if (request.getFlowerAmount() == null) {
            return MapConfig.buildMapKey(error, "Price is required");
        } else if (request.getFlowerAmount() <= 0) {
            return MapConfig.buildMapKey(error, "Price amount must be greater than 0");
        }

        if (request.getQuantity() < 0) {
            return MapConfig.buildMapKey(error, "Quantity must be greater than 0");
        }

        return error;
    }


}
