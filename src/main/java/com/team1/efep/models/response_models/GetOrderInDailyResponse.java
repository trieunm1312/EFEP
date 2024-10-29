package com.team1.efep.models.response_models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetOrderInDailyResponse {

    private String status;

    private String message;

    List<OrderCount> orderCounts;

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
