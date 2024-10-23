package com.team1.efep.models.response_models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteFlowerImageResponse {

    String status;

    String message;
}
