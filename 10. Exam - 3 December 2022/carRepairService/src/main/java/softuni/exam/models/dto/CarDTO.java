package softuni.exam.models.dto;

import softuni.exam.constants.CarType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "car")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarDTO {

    @XmlElement
    @Size(min = 2, max = 30)
    @NotNull
    private String carMake;

    @XmlElement
    @Size(min = 2, max = 30)
    @NotNull
    private String carModel;

    @XmlElement
    @Positive
    @NotNull
    private int year;

    @XmlElement
    @Size(min = 2, max = 30)
    @NotNull
    private String plateNumber;

    @XmlElement
    @Positive
    @NotNull
    private int kilometers;

    @XmlElement
    @Min(1)
    @NotNull
    private double engine;

    @XmlElement
    @NotNull
    private CarType carType;
}
