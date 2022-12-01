package softuni.exam.domain.dtos;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerExportDTO {

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private int number;

    @Expose
    private BigDecimal salary;

    @Expose
    private String teamName;

    @Override
    public String toString() {
        return "Player name: " + firstName + " " + lastName + System.lineSeparator() +
                "   Number: " + number + System.lineSeparator() +
                "   Salary: " + salary + System.lineSeparator() +
                "   Team: " + teamName + System.lineSeparator();
    }
}
