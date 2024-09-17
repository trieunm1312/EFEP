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
@Table(name = "`wishlist_item`")
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "`wishlist_id`")
    private Wishlist wishlist;

    @ManyToOne
    @JoinColumn(name = "`flower_id`")
    private Flower flower;

    private int quantity;
}
