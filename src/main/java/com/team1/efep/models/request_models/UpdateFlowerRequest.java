package com.team1.efep.models.request_models;

import com.team1.efep.models.entity_models.FlowerCategory;
import com.team1.efep.models.entity_models.FlowerImage;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateFlowerRequest {
    private int accountId;

    private int flowerId;

    private String name;

    private float price;

    private String description;

    private String status;

    private int flowerAmount;

    private int quantity;

    private List<FlowerImage> flowerImageList;

    private List<FlowerCategory> flowerCategoryList;
}
