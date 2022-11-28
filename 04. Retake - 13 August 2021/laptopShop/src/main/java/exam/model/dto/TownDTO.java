package exam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@XmlRootElement(name = "town")
@XmlAccessorType(XmlAccessType.FIELD)
public class TownDTO {

    @XmlElement
    @Size(min = 2)
    private String name;

    @XmlElement
    @Positive
    private Integer population;

    @XmlElement(name = "travel-guide")
    @Size(min = 10)
    private String travelGuide;

}
