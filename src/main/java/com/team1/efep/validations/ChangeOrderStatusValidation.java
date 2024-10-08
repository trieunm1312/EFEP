package com.team1.efep.validations;

import com.team1.efep.enums.Status;
import com.team1.efep.models.request_models.ChangeOrderStatusRequest;
import com.team1.efep.models.request_models.ChangePasswordRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeOrderStatusValidation {
    public static Map<String, String> validate(ChangeOrderStatusRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (request.getOrderId() <= 0) {
            errors.put("orderId", "Invalid order ID");
        }

        if (request.getStatus() == null || request.getStatus().trim().isEmpty()) {
            errors.put("status", "Order status is required");
        }

        return errors;
    }
}
