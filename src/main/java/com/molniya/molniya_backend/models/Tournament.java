package com.molniya.molniya_backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "tournaments")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

    private OffsetDateTime createdAt;
}
