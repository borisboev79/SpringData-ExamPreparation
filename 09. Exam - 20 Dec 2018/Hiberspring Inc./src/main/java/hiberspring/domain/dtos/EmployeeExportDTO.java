package hiberspring.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeExportDTO {

    private String firstName;

    private String lastName;

    private String position;

    private String cardNumber;

    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName + System.lineSeparator() +
               "Position: " + position + System.lineSeparator() +
               "Card Number: " + cardNumber + System.lineSeparator() +
               "-------------------------";
    }
}
