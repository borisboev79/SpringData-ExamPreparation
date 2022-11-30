package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "offers")
public class Offer extends AutoId{


    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "added_on", nullable = false)
    private LocalDateTime addedOn;

    @Column(name = "has_gold_status")
    private boolean hasGoldStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Car car;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Seller seller;

    @ManyToMany
    @JoinTable(name = "offers_pictures", joinColumns = @JoinColumn(name = "offer_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "picture_id", referencedColumnName = "id"))
    @Fetch(FetchMode.JOIN)
    private List<Picture> pictures;






}
