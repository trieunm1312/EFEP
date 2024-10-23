package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`purchased_plan`")
public class PurchasedPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "`payment_method_id`")
    private PaymentMethod paymentMethod;

    private String status;

    @ManyToOne
    @JoinColumn(name = "`seller_id`")
    private Seller seller;

    private String name;

    @Column(name = "`purchased_date`")
    private LocalDateTime purchasedDate;

    private float price;

}
