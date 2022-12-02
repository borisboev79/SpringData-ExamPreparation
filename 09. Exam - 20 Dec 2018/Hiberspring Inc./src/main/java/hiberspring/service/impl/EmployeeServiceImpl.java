package hiberspring.service.impl;

import hiberspring.domain.dtos.EmployeeDTO;
import hiberspring.domain.dtos.EmployeeExportDTO;
import hiberspring.domain.dtos.wrappers.EmployeesWrapperDTO;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.EmployeeService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static hiberspring.common.Constants.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeCardRepository cardRepository;
    private final BranchRepository branchRepository;
    private final FileUtil fileReader;
    private final XmlParser parser;
    private final ValidationUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeCardRepository cardRepository, BranchRepository branchRepository, FileUtil fileReader, XmlParser parser, ValidationUtil validator, ModelMapper mapper, StringBuilder result) {
        this.employeeRepository = employeeRepository;
        this.cardRepository = cardRepository;
        this.branchRepository = branchRepository;
        this.fileReader = fileReader;
        this.parser = parser;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return this.fileReader.readFile(PATH_TO_FILES + "employees.xml");
    }

    @Override
    public String importEmployees() throws JAXBException, FileNotFoundException {
        EmployeesWrapperDTO employeesDto = parser
                .parseXml(EmployeesWrapperDTO.class, PATH_TO_FILES + "employees.xml");


        for (EmployeeDTO employeeDto : employeesDto.getEmployees()) {

            boolean isValid = this.validator.isValid(employeeDto);

            if ((this.employeeRepository.findFirstByFirstNameAndLastNameAndPosition(
                            employeeDto.getFirstName(),
                            employeeDto.getLastName(),
                            employeeDto.getPosition())
                    .isPresent()) ||
                    (this.employeeRepository.findFirstByEmployeeCard_Number(
                                    employeeDto.getCard())
                            .isPresent()) ||
                    (this.cardRepository.findFirstByNumber(employeeDto.getCard()).isEmpty() ||
                            (this.branchRepository.findFirstByName(employeeDto.getBranch()).isEmpty())
                    )
            ) {
                isValid = false;
            }

            if (isValid) {

                Employee employee = this.mapper.map(employeeDto, Employee.class);

                EmployeeCard card = this.cardRepository.findFirstByNumber(employeeDto.getCard()).get();
                Branch branch = this.branchRepository.findFirstByName(employeeDto.getBranch()).get();

                employee.setEmployeeCard(card);
                employee.setBranch(branch);

                this.employeeRepository.save(employee);

                result.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,
                        employee.getClass().getSimpleName(),
                        employee.getFirstName() + " " + employee.getLastName()));
                result.append(System.lineSeparator());

            } else {
                result.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public String exportProductiveEmployees() {

        return this.employeeRepository.findAllByBranchWithProducts().orElseThrow(NoSuchElementException::new)
                .stream()
                .map(employee -> mapper.map(employee, EmployeeExportDTO.class).toString())
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
