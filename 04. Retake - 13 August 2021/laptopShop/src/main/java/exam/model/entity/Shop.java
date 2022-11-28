package exam.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shops")
public class Shop extends AutoId{

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private BigDecimal income;

    @Column(nullable = false)
    private String address;

    @Column(name = "employee_count", nullable = false)
    private Integer employeeCount;

    @Column(name = "shop_area", nullable = false)
    private Integer shopArea;

    @ManyToOne(optional = false)
    @JoinColumn(name = "town_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Town town;
}
