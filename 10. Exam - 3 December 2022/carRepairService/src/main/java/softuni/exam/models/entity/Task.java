package softuni.exam.models.entity;

import softuni.exam.constants.AutoId;
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
@Table(name = "tasks")
public class Task extends AutoId {

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Car car;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mechanic_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Mechanic mechanic;

    @ManyToOne(optional = false)
    @JoinColumn(name = "part_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Part part;

}
