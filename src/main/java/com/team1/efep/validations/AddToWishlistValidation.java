package com.team1.efep.validations;

import com.team1.efep.models.request_models.AddToWishlistRequest;

import java.util.HashMap;
import java.util.Map;

public class AddToWishlistValidation {

    public static Map<String, String> validate(AddToWishlistRequest request) {
        Map<String, String> errors = new HashMap<>();
        //validation code here
        //check account existed, role must be buyer, check flower existed, check flower status, check flower quantity
        return errors;
    }
}
