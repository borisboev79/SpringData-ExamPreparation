package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.dto.JobExportDTO;
import softuni.exam.models.entity.Job;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findFirstByTitleAndHoursAWeekAndSalary(String title, double hours, double salary);

    @Query("SELECT new softuni.exam.models.dto.JobExportDTO(j.title, j.salary, j.hoursAWeek)" +
            " FROM Job j " +
            " WHERE j.salary >= 5000 AND j.hoursAWeek <= 30" +
            " ORDER BY j.salary DESC")
    Optional<List<JobExportDTO>> findAllJobsOrderBySalaryDesc();

}
