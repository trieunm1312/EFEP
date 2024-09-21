package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`business_plan`")
public class BusinessPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private float price;

    private int duration;

    private String status;

    @OneToMany(mappedBy = "businessPlan")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Seller> sellerList;

    @OneToMany(mappedBy = "businessPlan")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PlanService> planServiceList;

}
