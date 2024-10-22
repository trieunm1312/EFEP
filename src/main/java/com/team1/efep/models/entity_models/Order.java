package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "`payment_method_id`")
    private PaymentMethod paymentMethod;

    private String status;

    @ManyToOne
    @JoinColumn(name = "`buyer_id`")
    private User user;

    @Column(name = "`buyer_name`")
    private String buyerName;

    @Column(name = "`created_date`")
    private LocalDateTime createdDate;

    @Column(name = "`total_price`")
    private float totalPrice;

    @OneToMany(mappedBy = "order")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrderDetail> orderDetailList;

}
