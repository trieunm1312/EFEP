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
public class FilterCategoryResponse {

    private String status;

    private String message;

    private int categoryId;

    private List<Flower> flowers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Flower{

        private String name;
    }

}
