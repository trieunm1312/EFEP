package com.team1.efep.models.response_models;

import com.team1.efep.models.request_models.CreateBusinessPlanRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBusinessPlanResponse {

    private String status;

    private String message;

    private String name;

    private String description;

    private float price;

    private int duration;

    private String plansStatus;

    private List<BusinessService> businessServiceList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BusinessService{

        private int id;

        private String name;

    }
}
