package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.MechanicDTO;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.service.MechanicService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_MECHANIC;
import static softuni.exam.constants.Messages.VALID_MECHANIC_FORMAT;
import static softuni.exam.constants.Paths.MECHANICS_JSON_PATH;

@Service
public class MechanicServiceImpl implements MechanicService {
    private final MechanicRepository mechanicRepository;
    private final Gson gson;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public MechanicServiceImpl(MechanicRepository mechanicRepository, Gson gson,
                               ValidationUtils validator, ModelMapper mapper,
                               StringBuilder result) {
        this.mechanicRepository = mechanicRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return Files.readString(Path.of(MECHANICS_JSON_PATH));
    }

    @Override
    public String importMechanics() throws IOException {
        List<MechanicDTO> mechanics = Arrays.stream(gson.fromJson(readMechanicsFromFile(), MechanicDTO[].class)).toList();

        for (MechanicDTO mechanicDto : mechanics) {
            boolean isValid = this.validator.isValid(mechanicDto);

            if (this.mechanicRepository.findFirstByFirstNameOrEmail(mechanicDto.getFirstName(), mechanicDto.getEmail()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Mechanic mechanic = this.mapper.map(mechanicDto, Mechanic.class);

                this.mechanicRepository.save(mechanic);

                result.append(String.format(VALID_MECHANIC_FORMAT,
                        mechanicDto.getFirstName(),
                        mechanicDto.getLastName()));


            } else {
                result.append(INVALID_MECHANIC).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
