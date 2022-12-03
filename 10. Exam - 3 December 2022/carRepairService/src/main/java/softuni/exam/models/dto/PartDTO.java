package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartDTO {

    @Size(min = 2, max = 20)
    @NotNull
    private String partName;

    @Min(10)
    @Max(2000)
    @NotNull
    private double price;

    @Positive
    @NotNull
    private int quantity;
}
