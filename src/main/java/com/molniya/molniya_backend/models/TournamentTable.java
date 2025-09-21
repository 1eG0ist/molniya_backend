package com.molniya.molniya_backend.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tournament_table")
public class TournamentTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    private Integer played = 0;
    private Integer wins = 0;
    private Integer draws = 0;
    private Integer losses = 0;
    private Integer points = 0;
}
