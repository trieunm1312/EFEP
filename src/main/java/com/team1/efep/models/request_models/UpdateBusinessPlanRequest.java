package com.team1.efep.models.request_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBusinessPlanRequest {

    private int id;

    private String name;

    private String description;

    private float price;

    private int duration;

    private String status;

    private List<Integer> businessServiceList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BusinessService {

        private int id;

    }
}
