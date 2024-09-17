package com.team1.efep.validations;

import com.team1.efep.models.request_models.ChangeOrderStatusRequest;
import com.team1.efep.models.request_models.DeleteCartItemRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteCartItemValidation {
    public static Map<String, String> validate(DeleteCartItemRequest request) {
        Map<String, String> errors = new HashMap<>();
        //validation code here
        return errors;
    }
}
