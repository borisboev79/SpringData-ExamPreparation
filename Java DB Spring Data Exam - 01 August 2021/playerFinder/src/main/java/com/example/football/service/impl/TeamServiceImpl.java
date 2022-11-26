package com.example.football.service.impl;

import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.models.entity.dto.TeamDTO;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;

import static com.example.football.util.constant.FilePaths.TEAMS_IMPORT_JSON_PATH;
import static com.example.football.util.constant.OutputMessages.INVALID_TEAM;
import static com.example.football.util.constant.OutputMessages.SUCCESSFULLY_ADDED_FORMAT;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final Gson gson;
    private final ModelMapper mapper;
    private final Validator validator;
    private final StringBuilder result;
    private final TownRepository townRepository;

    public TeamServiceImpl(TeamRepository teamRepository, Gson gson, ModelMapper mapper, Validator validator, StringBuilder result, TownRepository townRepository) {
        this.teamRepository = teamRepository;
        this.gson = gson;
        this.mapper = mapper;
        this.validator = validator;
        this.result = result;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(TEAMS_IMPORT_JSON_PATH));
    }

    @Override
    public String importTeams() throws IOException {
        TeamDTO[] teams = gson.fromJson(this.readTeamsFileContent(), TeamDTO[].class);

        for (TeamDTO teamDto : teams) {
            if (this.validator.validate(teamDto).isEmpty()){
                if(this.teamRepository.findFirstByName(teamDto.getName()).isEmpty()){
                    Team team = mapper.map(teamDto, Team.class);
                    Town town = this.townRepository.findByName(teamDto.getTownName()).get();
                    team.setTown(town);
                    this.teamRepository.save(team);
                    result.append(String.format(SUCCESSFULLY_ADDED_FORMAT, team.getClass().getSimpleName(), teamDto.getName(), teamDto.getFanBase()));
                }
                result.append(INVALID_TEAM).append(System.lineSeparator());
            } else {
                result.append(INVALID_TEAM).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
