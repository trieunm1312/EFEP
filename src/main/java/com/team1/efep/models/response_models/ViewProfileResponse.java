package com.team1.efep.models.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewProfileResponse {

    private String status;

    private String message;

    private int id;

    private String name;

    private String email;

    private String phone;

    private String avatar;

    private String background;
}
