package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "offers")
public class Offer extends AutoId{

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Agent agent;

    @ManyToOne(optional = false)
    @JoinColumn(name = "apartment_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Apartment apartment;

    @Column(name = "published_on", nullable = false)
    private LocalDate publishedOn;


    @Override
    public String toString() {
        return "Agent " + this.agent.getFirstName() + " " + this.agent.getLastName() +
                " with offer №" + getId() + ":" + System.lineSeparator() +
                "       -Apartment area: " + this.apartment.getArea() + System.lineSeparator() +
                "       --Town: " + this.apartment.getTown().getTownName() + System.lineSeparator() +
                "       ---Price: " + getPrice() + "$";
    }
}
