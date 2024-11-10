package com.team1.efep.models.response_models;

import com.team1.efep.models.entity_models.FlowerCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFlowerResponse {

    private String status;

    private String message;
//
//    private FlowerInfo flower;
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    public static class FlowerInfo {
//        private int id;
//
//        private String name;
//
//        private float price;
//
//        private float rating;
//
//        private String description;
//
//        private int flowerAmount;
//
//        private int quantity;
//
//        private int soldQuantity;
//
//        private LocalDateTime createDate;
//
//        private List<FlowerCategory> flowerCategoryList;
//
//        private List<Images> imageList;
//
//        @Data
//        @AllArgsConstructor
//        @NoArgsConstructor
//        @Builder
//        public static class Images {
//            private String link;
//        }
//    }


}
