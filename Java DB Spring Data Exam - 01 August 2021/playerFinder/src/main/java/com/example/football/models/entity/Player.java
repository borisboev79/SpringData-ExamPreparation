package com.example.football.models.entity;

import com.example.football.util.PlayerPosition;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "players")
public class Player extends AutoId{

    @Column(name = "first_name", nullable = false)

    private String firstName;

    @Column(name = "last_name", nullable = false)

    private String lastName;

    @Column(nullable = false, unique = true)

    private String email;

    //dd/MM/yyyy

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerPosition position;

    @ManyToOne(optional = false)
    @JoinColumn(name = "town_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Town town;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Team team;

    @ManyToOne(optional = false)
    @JoinColumn(name = "stat_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Stat stat;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(email, player.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Player - " + firstName + " " + lastName + System.lineSeparator() +
                "       Position - " + position + System.lineSeparator() +
                "       Team - " + team.getName() + System.lineSeparator() +
                "       Stadium - " + team.getStadiumName() + System.lineSeparator();
    }

}
