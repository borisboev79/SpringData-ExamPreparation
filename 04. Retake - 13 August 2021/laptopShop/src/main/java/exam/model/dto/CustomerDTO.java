package exam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    @Size(min = 2)
    private String firstName;

    @Size(min = 2)
    private String lastName;

    @Email
    private String email;

    private LocalDate registeredOn;

    private TownNameOnlyDTO town;
}
