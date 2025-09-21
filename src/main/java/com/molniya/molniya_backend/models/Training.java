package com.molniya.molniya_backend.models;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private OffsetDateTime createdAt = OffsetDateTime.now();
}

