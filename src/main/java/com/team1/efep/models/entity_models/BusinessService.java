package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`business_service`")
public class BusinessService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private float price;

    @OneToMany(mappedBy = "businessService")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PlanService> planServiceList;

}
