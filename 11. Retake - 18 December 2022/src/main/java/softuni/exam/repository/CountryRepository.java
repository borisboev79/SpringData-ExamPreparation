package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Country;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {


    Optional<Country> findFirstByName(String name);
    Optional<Country> findFirstById(Long id);
}
