package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "`flower`")
public class Flower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "`status_id`")
    private FlowerStatus flowerStatus;

    @ManyToOne
    @JoinColumn(name = "`seller_id`")
    private Seller seller;

    private String name;

    private float price;

    private float rating;

    private String description;

    private int quantity;

    private int soldQuantity;

    @OneToMany(mappedBy = "flower")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FlowerImage> flowerImageList;

    @OneToMany(mappedBy = "flower")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FlowerCategory> flowerCategoryList;

    @OneToMany(mappedBy = "flower")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CartItem> cartItemList;

    @OneToMany(mappedBy = "flower")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Feedback> feedbackList;

    @OneToMany(mappedBy = "flower")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrderDetail> orderDetailList;
}
