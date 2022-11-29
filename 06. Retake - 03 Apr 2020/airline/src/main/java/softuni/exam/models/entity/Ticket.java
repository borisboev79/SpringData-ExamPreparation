package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket extends AutoId{

    @Column(name = "serial_number", nullable = false, unique = true )
    private String serialNumber;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "take_off", nullable = false)
    private LocalDateTime takeoff;

    @ManyToOne(optional = false)
    @JoinColumn(name = "from_town_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Town fromTown;

    @ManyToOne(optional = false)
    @JoinColumn(name = "to_town_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Town toTown;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plane_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Plane plane;

    @ManyToOne(optional = false)
    @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Passenger passenger;

}
