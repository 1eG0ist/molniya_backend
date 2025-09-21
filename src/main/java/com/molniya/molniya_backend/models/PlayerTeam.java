package com.molniya.molniya_backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "players_teams",
        uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "team_id"}))
public class PlayerTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    private OffsetDateTime joinedAt;
}
