package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.CustomerDTO;
import exam.model.entity.Customer;
import exam.model.entity.Town;
import exam.repository.CustomerRepository;
import exam.repository.TownRepository;
import exam.service.CustomerService;
import exam.util.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static exam.util.constants.Messages.INVALID_CUSTOMER;
import static exam.util.constants.Messages.VALID_CUSTOMER_FORMAT;
import static exam.util.constants.Paths.CUSTOMERS_JSON_PATH;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final TownRepository townRepository;
    private final Gson gson;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, TownRepository townRepository,
                               @Qualifier("dateConverter") Gson gson, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.customerRepository = customerRepository;
        this.townRepository = townRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(Path.of(CUSTOMERS_JSON_PATH));
    }

    @Override
    public String importCustomers() throws IOException {

        List<CustomerDTO> customersDto = Arrays.stream(gson.fromJson(readCustomersFileContent(), CustomerDTO[].class)).toList();

        for (CustomerDTO customerDto : customersDto) {
            boolean isValid = this.validator.isValid(customerDto);

            if (this.customerRepository.findFirstByEmail(customerDto.getEmail()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Customer customer = this.mapper.map(customerDto, Customer.class);

                Town town = this.townRepository.findFirstByName(customerDto.getTown().getName()).get();

                customer.setTown(town);

                this.customerRepository.save(customer);

                result.append(String.format(VALID_CUSTOMER_FORMAT,
                        customerDto.getFirstName(),
                        customerDto.getLastName(),
                        customerDto.getEmail()));


            } else {
                result.append(INVALID_CUSTOMER).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
