package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobExportDTO {

    private String title;

    private double salary;

    private double hoursAWeek;

    @Override
    public String toString() {
        return "Job title " + title + System.lineSeparator() +
                "-Salary: " + salary + "$" + System.lineSeparator() +
                String.format("--Hours a week: %.2fh.", hoursAWeek) + System.lineSeparator();
    }
}
