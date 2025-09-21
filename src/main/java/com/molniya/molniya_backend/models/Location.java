package com.molniya.molniya_backend.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;
}

