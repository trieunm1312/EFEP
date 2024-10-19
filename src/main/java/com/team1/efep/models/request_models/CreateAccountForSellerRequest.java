package com.team1.efep.models.request_models;

import com.team1.efep.models.entity_models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountForSellerRequest {

    private String name;

    private String email;

    private String phone;

    private String password;

    private String confirmPassword;

}
