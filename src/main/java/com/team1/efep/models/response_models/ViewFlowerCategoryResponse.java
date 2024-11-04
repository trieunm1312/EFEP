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
public class ViewFlowerCategoryResponse {

    private String status;

    private String message;

    private List<CategoryDetail> categoryList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryDetail {

        private int id;

        private String name;

    }

}
