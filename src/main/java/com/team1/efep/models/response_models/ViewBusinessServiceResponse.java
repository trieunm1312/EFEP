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
public class ViewBusinessServiceResponse {

    private String status;

    private String message;

    private List<Services> servicesList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Services {

        private int id;

        private String name;

        private String description;

        private float price;
    }

}
