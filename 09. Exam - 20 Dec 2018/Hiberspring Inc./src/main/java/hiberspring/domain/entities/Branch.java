package hiberspring.domain.entities;

import hiberspring.common.AutoId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "branches")
public class Branch extends AutoId {

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "town_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Town town;

    @OneToMany(mappedBy = "branch", targetEntity = Product.class)
    @Fetch(FetchMode.JOIN)
    private List<Product> products;

}

