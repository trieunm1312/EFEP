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
public class ViewSellerTopListResponse {
    private String status;
    private String message;
    private List<Seller> sellerList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Seller {
        private int id;
        private String name;
        private String avatar;
        private double averageRating;
    }
}
