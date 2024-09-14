package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`seller`")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`user_id`")
    private User user;

    @ManyToOne
    @JoinColumn(name = "`plan_id`")
    private BusinessPlan businessPlan;

    @Column(name = "`plan_purchase_date`")
    private LocalDateTime planPurchaseDate;

    @OneToMany(mappedBy = "seller")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Flower> flowerList;

    @OneToMany(mappedBy = "seller")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PurchasedPlan> purchasedPlanList;

}
