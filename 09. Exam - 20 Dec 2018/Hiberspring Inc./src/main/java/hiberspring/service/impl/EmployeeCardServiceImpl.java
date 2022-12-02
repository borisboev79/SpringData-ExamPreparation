package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.EmployeeCardDTO;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static hiberspring.common.Constants.*;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {
    private final EmployeeCardRepository employeeCardRepository;
    private final FileUtil fileReader;
    private final Gson gson;
    private final ValidationUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;


    @Autowired
    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository, FileUtil fileReader, Gson gson, ValidationUtil validator, ModelMapper mapper, StringBuilder result) {
        this.employeeCardRepository = employeeCardRepository;
        this.fileReader = fileReader;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public Boolean employeeCardsAreImported() {
        return this.employeeCardRepository.count() > 0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return this.fileReader.readFile(PATH_TO_FILES + "employee-cards.json");
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) throws IOException {
        List<EmployeeCardDTO> emoloyees = Arrays.stream(gson.fromJson(readEmployeeCardsJsonFile(), EmployeeCardDTO[].class)).toList();

        for (EmployeeCardDTO cardDto : emoloyees) {
            boolean isValid = this.validator.isValid(cardDto);

            if (this.employeeCardRepository.findFirstByNumber(cardDto.getNumber()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                EmployeeCard card = this.mapper.map(cardDto, EmployeeCard.class);

                this.employeeCardRepository.save(card);

                result.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,
                        "Employee Card",
                        card.getNumber()));
                result.append(System.lineSeparator());


            } else {
                result.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
