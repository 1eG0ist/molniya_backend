package com.molniya.molniya_backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "player_distances")
public class PlayerDistance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal distance;

    @Column(nullable = false)
    private Duration time;

    private OffsetDateTime measuredAt = OffsetDateTime.now();
}
