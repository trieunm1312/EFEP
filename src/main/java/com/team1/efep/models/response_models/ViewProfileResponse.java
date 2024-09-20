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
    private String email;
    //private String password; ==> đảm bảo sự bảo mật của khách hàng ko nên trả về pass
    private String role;
    private String accountStatus;
}
