package com.team1.efep.models.response_models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewSellerApplicationResponse {
    int id;
    String content;
    String rejectionReason;
    String status;
    String createdDate;
    String approvedDate;
    int accountId;
    String buyerName;
    String buyerPhone;

}
