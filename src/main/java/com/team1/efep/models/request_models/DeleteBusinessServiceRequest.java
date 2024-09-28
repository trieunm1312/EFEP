package com.team1.efep.models.request_models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteBusinessServiceRequest {
    private int id;
}
