package com.team1.efep.models.request_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequest {

    private int id;

    private String name;

    private String phone;

    private String avatar;

    private String background;
}
