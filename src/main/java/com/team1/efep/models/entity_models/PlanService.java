package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`plan_service`")
public class PlanService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "`plan_id`")
    private BusinessPlan businessPlan;

    @ManyToOne
    @JoinColumn(name = "`service_id`")
    private BusinessService businessService;
    ;
}
