package com.team1.efep.models.request_models;

import com.team1.efep.models.response_models.ViewBusinessPlanResponse;
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

    private int id;

    private String name;

    private String description;

    private float price;

    private int duration;

    private String status;

    private List<BusinessService> businessServiceList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BusinessService {

        private int id;

    }
}
