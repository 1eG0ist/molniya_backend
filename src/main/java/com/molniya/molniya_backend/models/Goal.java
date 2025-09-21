package com.molniya.molniya_backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false, length = 1023)
    private String description;

    private LocalDate targetDate;

    private Boolean isCompleted = false;
}
