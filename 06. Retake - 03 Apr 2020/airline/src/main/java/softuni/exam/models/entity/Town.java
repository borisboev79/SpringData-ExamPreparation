package softuni.exam.models.entity;

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
@Table(name = "towns")
public class Town extends AutoId {

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private int population;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String guide;
}
