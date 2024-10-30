package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.FilterOrderRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FilterOrderValidation {
    public static Map<String, String> validate(FilterOrderRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (request.getSellerId() <= 0) {
            return MapConfig.buildMapKey(errors,  "Invalid account ID");
        }
        return errors;
    }
}
