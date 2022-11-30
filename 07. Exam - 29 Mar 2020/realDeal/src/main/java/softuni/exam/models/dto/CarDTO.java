package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    @Size(min = 2, max = 20)
    private String make;

    @Size(min = 2, max = 20)
    private String model;

    @Positive
    private int kilometers;

    private LocalDate registeredOn;

}
