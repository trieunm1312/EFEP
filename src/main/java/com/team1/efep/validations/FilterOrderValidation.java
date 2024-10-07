package com.team1.efep.validations;

import com.team1.efep.models.request_models.FilterOrderRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FilterOrderValidation {
    public static Map<String, String> validate(FilterOrderRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (request.getAccountId() <= 0) {
            errors.put("accountId", "Invalid account ID");
        }

        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                errors.put("status", "Invalid order status");
        }

        if (request.getCreatedDate() != null) {
            if (request.getCreatedDate().isAfter(LocalDateTime.now())) {
                errors.put("createdDate", "Created date cannot be in the future");
            }
        }
        return errors;
    }
}
