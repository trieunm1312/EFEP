package com.team1.efep.models.entity_models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private String status;

    @ManyToOne
    @JoinColumn(name = "`seller_id`")
    private Seller seller;

    private String name;

    private float price;

    private String description;

    @Column(name = "`flower_amount`")
    private int flowerAmount;

    private int quantity;

    @Column(name = "`sold_quantity`")
    private int soldQuantity;

    @Column(name = "`create_date`")
    private LocalDateTime createDate;

    @Column(name = "`withering_date`")
    private LocalDate witheringDate;

    @OneToMany(mappedBy = "flower", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FlowerImage> flowerImageList;

    @OneToMany(mappedBy = "flower", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FlowerCategory> flowerCategoryList;

    @OneToMany(mappedBy = "flower", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<WishlistItem> wishlistItemList;

    @OneToMany(mappedBy = "flower", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrderDetail> orderDetailList;

}
