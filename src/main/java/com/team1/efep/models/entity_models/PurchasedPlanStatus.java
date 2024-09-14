package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`purchased_plan_status`")
public class PurchasedPlanStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String status;

    @OneToMany(mappedBy = "purchasedPlanStatus")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PurchasedPlan> purchasedPlanList;
}
