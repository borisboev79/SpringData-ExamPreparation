package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.util.LocalDateAdapter;

import javax.persistence.Column;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "apartment")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferDTO {

    @Positive
    private BigDecimal price;

    @XmlElement(name = "agent")
    private AgentNameOnlyDTO agent;

    @XmlElement(name = "apartment")
    private ApartmentIdOnlyDTO apartment;

    @Column(name = "published_on", nullable = false)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate publishedOn;
}
