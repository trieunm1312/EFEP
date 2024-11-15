package com.team1.efep.models.request_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFlowerRequest {

    private int accountId;

    private String name;

    private Float price;

    private String description;

    private Integer flowerAmount;

    private Integer quantity;

    private LocalDate witheringDate;

    private List<String> imageList;

    private List<Integer> categoryIdList;
}
