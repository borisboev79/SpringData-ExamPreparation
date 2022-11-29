package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.util.LocalDateAdapter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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
@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketDTO {

    @XmlElement(name = "serial-number")
    @NotNull
    @Size(min = 2)
    private String serialNumber;

    @XmlElement
    @NotNull
    @Positive
    private BigDecimal price;

    @XmlElement(name = "take-off")
    @NotNull
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDateTime takeoff;

    @XmlElement(name = "from-town")
    private FromTownNameOnlyDTO fromTown;

   @XmlElement(name = "to-town")
    private ToTownNameOnlyDTO toTown;

    @XmlElement
    private PassengerEmailOnlyDTO passenger;

    @XmlElement
    private PlaneSerialNumberOnlyDTO plane;



}
