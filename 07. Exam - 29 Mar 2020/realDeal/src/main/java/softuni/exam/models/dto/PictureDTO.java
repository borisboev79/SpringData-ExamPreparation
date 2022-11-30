package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PictureDTO {

    @NotNull
    @Size(min = 2, max = 20)
    private String name;

    @NotNull
    private LocalDateTime dateAndTime;

    @NotNull
    private Long car;


}
