package softuni.exam.repository;

import softuni.exam.constants.CarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  Optional<List<Task>> findAllByCar_CarTypeOrderByPriceDesc(CarType coupe);

}
