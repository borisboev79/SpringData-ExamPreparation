package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "job")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobDTO {

    @XmlElement(name = "jobTitle")
    @NotNull
    @Size(min = 2, max = 40)
    private String title;

    @XmlElement
    @NotNull
    @Min(10)
    private double hoursAWeek;


    @XmlElement
    @NotNull
    @Min(300)
    private double salary;

    @XmlElement
    @NotNull
    @Size(min = 5)
    private String description;

    @XmlElement(name = "companyId")
    @NotNull
    private Long company;
}
