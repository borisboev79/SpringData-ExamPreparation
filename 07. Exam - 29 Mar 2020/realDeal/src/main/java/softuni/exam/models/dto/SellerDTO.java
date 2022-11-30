package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.util.constants.Rating;

import javax.validation.constraints.Email;
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
@XmlRootElement(name = "seller")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerDTO {

    @XmlElement(name = "first-name")
    @Size(min = 2, max = 20)
    @NotNull
    private String firstName;

    @XmlElement(name = "last-name")
    @Size(min = 2, max = 20)
    @NotNull
    private String lastName;

    @Email
    @NotNull
    private String email;

    @XmlElement
    @NotNull
    private Rating rating;

    @XmlElement
    @NotNull
    private String town;
}
