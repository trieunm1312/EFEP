package com.team1.efep.models.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewFlowerImageResponse {

    private String status;

    private String message;

    private List<FlowerImageLink> images;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FlowerImageLink {
        private String link;
    }
}
