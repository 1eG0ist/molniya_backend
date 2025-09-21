package com.molniya.molniya_backend.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_sensitive_data")
public class UserSensitiveData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 63)
    private String password;
}