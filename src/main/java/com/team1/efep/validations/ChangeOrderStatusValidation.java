package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.ChangeOrderStatusRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangeOrderStatusValidation {
    public static Map<String, String> validate(ChangeOrderStatusRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (request.getOrderId() <= 0) {
            return MapConfig.buildMapKey(errors, "Invalid order ID");
        }

        if (request.getStatus() == null || request.getStatus().trim().isEmpty()) {
            return MapConfig.buildMapKey(errors, "Order status is required");
        }

        return errors;
    }
}
