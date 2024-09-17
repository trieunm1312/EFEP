package com.team1.efep.models.response_models;

import com.team1.efep.models.entity_models.Flower;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewFlowerListResponse {
    private String status;
    private String message;
    private List<Flower> flowerList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Flower {
        private String name;
        private float price;
        private float rating;
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
