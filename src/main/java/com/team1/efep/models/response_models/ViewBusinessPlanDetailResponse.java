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
public class ViewBusinessPlanDetailResponse {

    private String status;

    private String message;

    private int id;

    private String name;

    private String description;

    private float price;

    private int duration;

    private String planStatus;

    private List<BusinessService> businessServiceList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BusinessService{

        private int id;

        private String name;

        private String description;

        private float price;
    }
}
