package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`cart`")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`user_id`")
    private User user;

    @OneToMany(mappedBy = "cart")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CartItem> cartItemList;

}