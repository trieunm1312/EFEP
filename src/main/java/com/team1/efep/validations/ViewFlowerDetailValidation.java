package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.ViewFlowerDetailRequest;
import com.team1.efep.repositories.FlowerRepo;

import java.util.HashMap;
import java.util.Map;

public class ViewFlowerDetailValidation {
    public static Map<String, String> validate(ViewFlowerDetailRequest request, FlowerRepo flowerRepo) {
        Map<String, String> error = new HashMap<>();
        //validation code here;

        if(!flowerRepo.existsById(request.getId())){
            return MapConfig.buildMapKey(error, "Flower does not exist");
        }
        return error;
    }
}
