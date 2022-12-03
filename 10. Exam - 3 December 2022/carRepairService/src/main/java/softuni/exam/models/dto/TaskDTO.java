package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.util.LocalDateTimeAdapter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskDTO {

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @NotNull
    private LocalDateTime date;

    @XmlElement
    @Positive
    @NotNull
    private BigDecimal price;

    @XmlElement
    @NotNull
    private CarIdOnlyDTO car;

    @XmlElement
    @NotNull
    private MechanicFirstNameOnlyDTO mechanic;

    @XmlElement
    @NotNull
    private PartIdOnlyDTO part;

}
