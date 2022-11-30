package softuni.exam.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cars")
public class Car extends AutoId{

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private int kilometers;

    @Column(nullable = false)
    private LocalDate registeredOn;

    @OneToMany(mappedBy = "car", targetEntity = Picture.class)
    private List<Picture> pictures;

    public Car(){
        this.pictures = new ArrayList<>();
    }

    public Car(String make, String model, int kilometers, LocalDate registeredOn){
        this();
        this.make = make;
        this.model = model;
        this.kilometers = kilometers;
        this.registeredOn = registeredOn;
    }

    @Override
    public String toString() {
        return "Car make - " + make + ", model - " + model + System.lineSeparator() +
                "        Kilometers - " + kilometers + System.lineSeparator() +
                "        Registered on - " + registeredOn + System.lineSeparator() +
                "        Number of pictures - " + pictures.size() + System.lineSeparator();
    }
}
