package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Part;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskExportDTO {

    private Long id;

    private BigDecimal price;

    private CarDTO car;

    private MechanicDTO mechanic;

    private PartDTO part;

    @Override
    public String toString() {
        return "Car " + car.getCarMake() + " " + car.getCarModel() + " with " + car.getKilometers() + "km" + System.lineSeparator() +
                "         -Mechanic: " + mechanic.getFirstName() + " " + mechanic.getLastName() + " - task №" + this.getId() + System.lineSeparator() +
                "         --Engine: " + car.getEngine() + System.lineSeparator() +
                "         ---Price: " + this.price + "$";

    }
}
