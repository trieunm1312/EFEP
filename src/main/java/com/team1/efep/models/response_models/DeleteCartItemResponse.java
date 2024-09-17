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
public class DeleteCartItemResponse {
    private String status;

    private String message;

    private List<Item> cart;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Item {
        private String id;
        private String name;
    }
}
