package com.team1.efep.models.entity_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @JoinColumn(name = "`flower_id`")
    private Flower flower;

    @ManyToOne
    @JoinColumn(name = "`status_id`")
    private FeedbackStatus feedbackStatus;

    private int rating;

    private String content;

    private LocalDateTime date;

    private boolean isReported;

    private String image;

}
