package com.team1.efep.models.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewFlowerListResponse {

    private String status;

    private String message;

    private String keyword;

    private List<Flower> flowerList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Flower {

        private int id;

        private String name;

        private float price;

        private String description;

        private LocalDate witheringDate;

        private LocalDate today;


        private int quantity;

        private int soldQuantity;

        private List<Image> images;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Image {

        private String link;
    }
}
