package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`seller_application`")
public class SellerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    @Column(name = "`rejection_reason`")
    private String rejectionReason;

    private String status;

    @Column(name = "`created_date`")
    private LocalDateTime createdDate;

    @Column(name = "`approved_date`")
    private LocalDateTime approvedDate;

    @ManyToOne
    @JoinColumn(name = "`user_id`")
    private User user;

}
