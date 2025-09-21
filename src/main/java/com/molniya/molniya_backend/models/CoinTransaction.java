package com.molniya.molniya_backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "coin_transactions")
public class CoinTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Float amount;

    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    private OffsetDateTime createdAt = OffsetDateTime.now();
}
