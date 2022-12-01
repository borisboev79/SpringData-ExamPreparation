package softuni.exam.domain.dtos;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.domain.dtos.PictureDTO;
import softuni.exam.domain.dtos.TeamDTO;
import softuni.exam.util.constants.Position;

import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {

    @Expose
    private String firstName;

    @Expose
    @Size(min = 3, max = 15)
    private String lastName;

    @Expose
    @Min(1)
    @Max(99)
    private int number;

    @Expose
    @NotNull
    @Min(0)
    private BigDecimal salary;

    @Expose
    @NotNull
    private Position position;

    @Expose
    private PictureJsonDTO picture;

    @Expose
    private TeamJsonDTO team;
}
