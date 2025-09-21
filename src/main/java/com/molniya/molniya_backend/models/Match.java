package com.molniya.molniya_backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @Column(name = "match_date", nullable = false)
    private OffsetDateTime matchDate;

    private Integer scoreHome;
    private Integer scoreAway;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
}
