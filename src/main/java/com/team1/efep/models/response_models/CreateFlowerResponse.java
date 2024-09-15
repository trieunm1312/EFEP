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
public class CreateFlowerResponse {

    private String status;

    private String message;

    private FlowerInfo flower;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FlowerInfo {
        private int id;

        private String name;

        private float price;

        private float rating;

        private String description;

        private int flowerAmount;

        private int quantity;

        private int soldQuantity;

        private List<Images> imageList;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class Images {
            private String link;
        }
    }


}
