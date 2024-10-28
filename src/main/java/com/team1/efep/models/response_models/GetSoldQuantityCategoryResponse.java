package com.team1.efep.models.response_models;

import com.team1.efep.models.entity_models.FlowerCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetSoldQuantityCategoryResponse {

    private String status;

    private String message;

    private Map<String, Long> soldQuantityByCategory;
}
