package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.entity.enums.DaysOfWeek;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "forecasts")
public class Forecast extends AutoId{

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DaysOfWeek dayOfWeek;

    @Column(name = "max_temperature", nullable = false)
    private Float maxTemperature;

    @Column(name = "min_temperature", nullable = false)
    private Float minTemperature;

    @Column(nullable = false)
    private LocalTime sunrise;

    @Column(nullable = false)
    private LocalTime sunset;

    @ManyToOne(optional = false)
    @JoinColumn(name ="city_id", referencedColumnName = "id")
    private City city;

    @Override
    public String toString() {
        return "City: " + city.getCityName() + ":" + System.lineSeparator() +
                "       -min temperature: " + minTemperature + System.lineSeparator() +
                "       --max temperature: " + maxTemperature + System.lineSeparator() +
                "       ---sunrise: " + sunrise + System.lineSeparator() +
                "       ----sunset: " + sunset;
    }
}
