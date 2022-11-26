package com.example.football.service.impl;

import com.example.football.models.entity.Town;
import com.example.football.models.entity.dto.TownDTO;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;

import static com.example.football.util.constant.FilePaths.TOWNS_IMPORT_JSON_PATH;
import static com.example.football.util.constant.OutputMessages.INVALID_TOWN;
import static com.example.football.util.constant.OutputMessages.SUCCESSFULLY_ADDED_FORMAT;


@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final Gson gson;
    private final ModelMapper mapper;
    private final Validator validator;
    private final StringBuilder result;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, Gson gson, ModelMapper mapper, Validator validator, StringBuilder result) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.mapper = mapper;

        this.validator = validator;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(TOWNS_IMPORT_JSON_PATH));
    }

    @Override
    public String importTowns() throws IOException {

        TownDTO[] towns = gson.fromJson(this.readTownsFileContent(), TownDTO[].class);

        for (TownDTO townDto : towns) {
           if (this.validator.validate(townDto).isEmpty()){
               if(this.townRepository.findByName(townDto.getName()).isEmpty()){
                   Town town = mapper.map(townDto, Town.class);
                   this.townRepository.save(town);
                   result.append(String.format(SUCCESSFULLY_ADDED_FORMAT, town.getClass().getSimpleName(), townDto.getName(), townDto.getPopulation()));
               }
               result.append(INVALID_TOWN).append(System.lineSeparator());
            } else {
               result.append(INVALID_TOWN).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
