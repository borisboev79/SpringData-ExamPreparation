package softuni.exam.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import softuni.exam.util.constants.Position;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "players")
public class Player extends AutoId {
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false)
    private BigDecimal salary;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

    @ManyToOne(optional = false)
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Picture picture;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Team team;


    @Override
    public String toString() {
        return "Player name: " + firstName + " " + lastName + " - " + position + System.lineSeparator() +
               "Number: " + number;
    }
}
