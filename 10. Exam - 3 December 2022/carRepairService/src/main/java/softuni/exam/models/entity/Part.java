package softuni.exam.models.entity;

import softuni.exam.constants.AutoId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parts")
public class Part extends AutoId {

    @Column(name = "part_name", nullable = false, unique = true)
    private String partName;

    @Column
    private double price;

    @Column
    private int quantity;


}
