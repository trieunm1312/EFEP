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
public class ViewFlowerDetailResponse {

    private String status;

    private String message;

    private Flower flower;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Flower {

        private int id;

        private String name;

        private float price;

        private int flower_amount;

        private int quantity;

        private int sold_quantity;

        private List<Image> imageList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Image {

        private String link;
    }
}
