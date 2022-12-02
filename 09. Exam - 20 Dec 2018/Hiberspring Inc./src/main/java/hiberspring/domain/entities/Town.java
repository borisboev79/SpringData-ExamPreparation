package hiberspring.domain.entities;

import hiberspring.common.AutoId;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int population;
}
