package com.molniya.molniya_backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "player_evaluations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "trainer_id", "training_id"}))
public class PlayerEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;

    @Column(nullable = false)
    private Integer score;

    private String note;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
