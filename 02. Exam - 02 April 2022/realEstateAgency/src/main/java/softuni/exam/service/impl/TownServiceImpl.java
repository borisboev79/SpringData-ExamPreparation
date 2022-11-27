package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TownDTO;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.util.constants.Messages.INVALID_TOWN;
import static softuni.exam.util.constants.Messages.VALID_TOWN_FORMAT;
import static softuni.exam.util.constants.Paths.TOWNS_JSON_PATH;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final Gson gson;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, Gson gson, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWNS_JSON_PATH));
    }

    @Override
    public String importTowns() throws IOException {
        List<TownDTO> towns = Arrays.stream(gson.fromJson(readTownsFileContent(), TownDTO[].class)).toList();

        for (TownDTO townDto : towns) {
            boolean isValid = this.validator.isValid(townDto);

            if (this.townRepository.findFirstByTownName(townDto.getTownName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Town town = this.mapper.map(townDto, Town.class);

                this.townRepository.save(town);

                result.append(String.format(VALID_TOWN_FORMAT,
                        townDto.getTownName(),
                        townDto.getPopulation()));


            } else {
                result.append(INVALID_TOWN).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
