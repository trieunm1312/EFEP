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
public class CreateFlowerRequest {

    private int accountId;

    private String name;

    private float price;

    private String description;

    private int flowerAmount;

    private int quantity;

    private List<String> imgList;


}
