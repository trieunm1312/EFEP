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
public class ViewBuyerListResponse {
    private String status;
    private String message;
    private List<Buyer> buyers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Buyer {
        private int id;
        private String name;
    }
}
