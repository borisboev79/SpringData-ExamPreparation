package softuni.exam.domain.dtos;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamJsonDTO {

    @Expose
    @Size(min = 3, max = 20)
    private String name;

    @Expose
    @NotNull
    private PictureJsonDTO picture;

}
