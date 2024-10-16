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

    private int id;

    private String email;

    private String password;

    private User user;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class User {

        private String name;

        private String phone;

        private String avatar;

        private String background;

        private LocalDate createdDate;
    }


}
