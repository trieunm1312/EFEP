package com.team1.efep.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleToken {
    private String access_token;
    private String token_type;
    private Long expires_in;
    private String refresh_token;
}
