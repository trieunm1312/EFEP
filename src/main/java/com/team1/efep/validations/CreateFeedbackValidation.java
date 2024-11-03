package com.team1.efep.validations;

import com.team1.efep.models.entity_models.User;
import com.team1.efep.models.request_models.CreateFeedbackRequest;
import com.team1.efep.repositories.OrderRepo;

import java.util.HashMap;
import java.util.Map;

public class CreateFeedbackValidation {
    public static Map<String, String> validate(CreateFeedbackRequest request, OrderRepo orderRepo, User user) {
        Map<String, String> error = new HashMap<>();


        return error;
    }
}
