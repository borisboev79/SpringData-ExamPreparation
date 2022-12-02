package hiberspring.domain.entities;

import hiberspring.common.AutoId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends AutoId {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int clients;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Branch branch;
}
