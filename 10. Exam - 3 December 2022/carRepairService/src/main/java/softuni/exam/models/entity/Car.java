package softuni.exam.models.entity;

import softuni.exam.constants.AutoId;
import softuni.exam.constants.CarType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
public class Car extends AutoId {

    @Column(name = "car_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private CarType carType;

    @Column(name = "car_make", nullable = false)
    private String carMake;

    @Column(name = "car_model", nullable = false)
    private String carModel;

    @Column
    private int year;

    @Column(name = "plate_number", nullable = false, unique = true)
    private String plateNumber;

    @Column
    private int kilometers;

    @Column
    private double engine;

}
