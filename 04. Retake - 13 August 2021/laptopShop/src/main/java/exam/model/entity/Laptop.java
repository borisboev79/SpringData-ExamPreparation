package exam.model.entity;

import exam.util.constants.WarrantyType;
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
@Table(name = "laptops")
public class Laptop extends AutoId{

    @Column(name = "mac_address", nullable = false, unique = true)
    private String macAddress;

    @Column(name = "cpu_speed", nullable = false)
    private Double cpuSpeed;

    @Column(nullable = false)
    private Integer ram;

    @Column(nullable = false)
    private Integer storage;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "warranty_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WarrantyType warrantyType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Shop shop;

    @Override
    public String toString() {
        return "Laptop - " + macAddress + System.lineSeparator() +
                "*Cpu speed - " + cpuSpeed + System.lineSeparator() +
                "**Ram - " + ram + System.lineSeparator() +
                "***Storage - " + storage + System.lineSeparator() +
                "***Price - " + price + System.lineSeparator() +
                "#Shop name - " + this.getShop().getName() + System.lineSeparator() +
                "##Town - " + this.getShop().getTown().getName() + System.lineSeparator();
    }
}
