package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.TownDTO;
import hiberspring.domain.entities.Town;
import hiberspring.repository.TownRepository;
import hiberspring.service.TownService;
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
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final FileUtil fileReader;
    private final Gson gson;
    private final ValidationUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, FileUtil fileReader, Gson gson, ValidationUtil validator, ModelMapper mapper, StringBuilder result) {
        this.townRepository = townRepository;
        this.fileReader = fileReader;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public Boolean townsAreImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return this.fileReader.readFile(PATH_TO_FILES + "towns.json");
    }

    @Override
    public String importTowns(String townsFileContent) throws IOException {
        List<TownDTO> towns = Arrays.stream(gson.fromJson(readTownsJsonFile(), TownDTO[].class)).toList();

        for (TownDTO townDto : towns) {
            boolean isValid = this.validator.isValid(townDto);

            if (this.townRepository.findFirstByName(townDto.getName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Town town = this.mapper.map(townDto, Town.class);

                this.townRepository.save(town);

                result.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,
                        town.getClass().getSimpleName(),
                        town.getName()));
                result.append(System.lineSeparator());


            } else {
                result.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
