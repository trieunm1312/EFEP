package com.team1.efep.models.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewFlowerListForSellerResponse {
    private String status;
    private String message;
    private List<Flower> flowerList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Flower{
        private int id;
        private String name;
        private float price;
        private String flower_amount;
        private int quantity;
        private int sold_quantity;
    }

}
