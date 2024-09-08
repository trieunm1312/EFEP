package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "`payment_method`")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String method;

    @OneToMany(mappedBy = "paymentMethod")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Order> orderList;
}
