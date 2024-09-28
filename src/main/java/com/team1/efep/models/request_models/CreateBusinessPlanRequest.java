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
public class CreateBusinessPlanRequest {
    private String name;
    private String description;
    private float price;
    private int duration;
    private List<BusinessPlanService> serviceList;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BusinessPlanService {
        private int id;
    }
}
