package com.team1.efep.validations;

import com.team1.efep.models.request_models.UpdateFlowerRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateFlowerValidation {
    public static Map<String, String> validate(UpdateFlowerRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (request.getFlowerId() <= 0) {
            errors.put("flowerId", "Invalid flower ID");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            errors.put("name", "Flower name is required");
        } else if (request.getName().length() < 3 || request.getName().length() > 30) {
            errors.put("name", "Flower name must be between 3 and 30 characters");
        }

        if (request.getPrice() <= 0) {
            errors.put("price", "Price must be greater than 0");
        }

        if (request.getFlowerAmount() < 0) {
            errors.put("flowerAmount", "Flower amount cannot be less than 0");
        }

        if (request.getQuantity() < 0) {
            errors.put("quantity", "Quantity must be greater than or equal to 0");
        }

        if (request.getStatus() == null) {
            errors.put("status", "Invalid flower status");
        }
        return errors;
    }
}
