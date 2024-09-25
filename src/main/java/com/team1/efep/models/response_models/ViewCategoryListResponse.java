package com.team1.efep.models.response_models;

import com.team1.efep.models.entity_models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewCategoryListResponse {
    private String status;
    private String message;
    private List<Category> categoryList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Category {
        private String name;
    }

}