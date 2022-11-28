package exam.model.dto.wrapper;

import exam.model.dto.TownDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "towns")
@XmlAccessorType(XmlAccessType.FIELD)
public class TownsWrapperDTO {

    @XmlElement(name = "town")
    private List<TownDTO> towns;
}
