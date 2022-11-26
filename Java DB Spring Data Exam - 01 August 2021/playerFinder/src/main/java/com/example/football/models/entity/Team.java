package com.example.football.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teams")
public class Team extends AutoId{

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "stadium_name", nullable = false, unique = true)
    private String stadiumName;

    @Column(name = "fan_base", nullable = false)
    private Integer fanBase;

    @Column(columnDefinition = "text", nullable = false)
    private String history;

    @ManyToOne(optional = false)
    @JoinColumn(name = "town_id", referencedColumnName = "id")
    private Town town;

    @OneToMany(mappedBy = "team", targetEntity = Player.class)
    private Set<Player> players;
}
