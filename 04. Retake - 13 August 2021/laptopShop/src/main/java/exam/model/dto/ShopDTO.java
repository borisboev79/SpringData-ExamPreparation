package exam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "shop")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShopDTO {

    @XmlElement
    @Size(min = 4)
    private String address;

    @XmlElement(name = "employee-count")
    @Min(1)
    @Max(50)
    private Integer employeeCount;

    @XmlElement
    @Min(20000)
    private BigDecimal income;

    @XmlElement
    @Size(min = 4)
    private String name;

    @XmlElement(name = "shop-area")
    @Min(150)
    private Integer shopArea;

    @XmlElement
    private TownXmlNameOnlyDTO town;
}
