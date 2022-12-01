package softuni.exam.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "teams")
public class Team extends AutoId{

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Picture picture;

    @OneToMany(mappedBy = "team", targetEntity = Player.class)
    private List<Player> players;

    public Team(){
        this.players = new ArrayList<>();
    }

    public Team(String name, Picture picture) {
        this();
        this.name = name;
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Team: " + name + System.lineSeparator() +
                players.stream()
                        .map(Player::toString)
                        .collect(Collectors.joining(System.lineSeparator()));

    }
}
