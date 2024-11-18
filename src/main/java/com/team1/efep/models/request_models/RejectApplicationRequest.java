package com.team1.efep.models.request_models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RejectApplicationRequest {

    int accountId;

    int applicationId;

    String rejectionReason;
}
