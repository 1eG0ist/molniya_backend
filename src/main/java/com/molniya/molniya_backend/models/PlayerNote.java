package com.molniya.molniya_backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "player_notes")
public class PlayerNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;

    @Column(nullable = false)
    private String note;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
