package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`feedback`")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "`user_id`")
    private User user;

    @ManyToOne
    @JoinColumn(name = "`seller_id`")
    private Seller seller;

    private LocalDateTime createDate;

    private String content;

    private int rating;

}
