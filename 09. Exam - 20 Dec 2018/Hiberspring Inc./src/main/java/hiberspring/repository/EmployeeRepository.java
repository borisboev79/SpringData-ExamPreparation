package hiberspring.repository;

import hiberspring.domain.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findFirstByFirstNameAndLastNameAndPosition(String firstName, String lastName, String position);

    Optional<Employee> findFirstByEmployeeCard_Number(String card);

    @Query("SELECT e FROM Employee AS e WHERE e.branch.products.size > 0 ORDER BY e.firstName ASC, e.lastName ASC, LENGTH(e.position) DESC")
    Optional<List<Employee>> findAllByBranchWithProducts();

}
