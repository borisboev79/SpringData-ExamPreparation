package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Offer;
import softuni.exam.util.constants.ApartmentType;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    Optional<List<Offer>> findAllByApartment_ApartmentTypeOrderByApartment_AreaDescPriceAsc(ApartmentType type);

}
