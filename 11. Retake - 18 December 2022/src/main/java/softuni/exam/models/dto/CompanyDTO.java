package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.util.LocalDateAdapter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "company")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompanyDTO {

    @XmlElement(name = "companyName")
    @NotNull
    @Size(min = 2, max = 40)
    private String name;

    @XmlElement
    @NotNull
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateEstablished;

    @XmlElement
    @NotNull
    @Size(min = 2, max = 30)
    private String website;

    @XmlElement(name = "countryId")
    @NotNull
    private Long country;
}
