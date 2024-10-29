package com.team1.efep.models.response_models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetOrderInDailyResponse {

    private String status;

    private String message;

    private List<OrderCount> orderCounts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderCount {

        String date;

        long count;
    }

}
