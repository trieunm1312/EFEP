package com.team1.efep.models.response_models;

import com.team1.efep.models.entity_models.FlowerCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetSoldQuantityCategoryResponse {

    private String status;

    private String message;

    List<soldQuantityCategory> soldQuantityCategories;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class soldQuantityCategory {

        long soldFlowerQuantity;

        String category;



    }
}
