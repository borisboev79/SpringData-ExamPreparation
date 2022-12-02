package hiberspring.domain.entities;

import hiberspring.common.AutoId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee_cards")
public class EmployeeCard extends AutoId {

    @Column(nullable = false, unique = true)
    private String number;

    @OneToOne(mappedBy = "employeeCard", targetEntity = Employee.class)
    private Employee employee;
}
