package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.TeamDTO;
import softuni.exam.domain.dtos.wrappers.TeamsWrapperDTO;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static softuni.exam.util.constants.Messages.*;
import static softuni.exam.util.constants.Paths.TEAMS_XML_PATH;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final PictureRepository pictureRepository;
    private final JAXBContext context;
    private final ValidatorUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;
    private final FileUtil fileReader;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, PictureRepository pictureRepository, @Qualifier("teamsContext") JAXBContext context, ValidatorUtil validator, ModelMapper mapper, StringBuilder result, FileUtil fileReader) {
        this.teamRepository = teamRepository;
        this.pictureRepository = pictureRepository;
        this.context = context;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
        this.fileReader = fileReader;
    }

    @Override
    public String importTeams() throws JAXBException, FileNotFoundException {
        Unmarshaller unmarshaller = this.context.createUnmarshaller();

        TeamsWrapperDTO teams = (TeamsWrapperDTO) unmarshaller
                .unmarshal(new FileReader(TEAMS_XML_PATH));


        for (TeamDTO teamDto : teams.getTeams()) {

            boolean isValid = this.validator.isValid(teamDto);

            if (this.teamRepository.findFirstByName(teamDto.getName()).isPresent() ||
            this.pictureRepository.findFirstByUrl(teamDto.getPicture().getUrl()).isEmpty()) {
                isValid = false;
            }

            if (isValid) {

                Team team = this.mapper.map(teamDto, Team.class);

                Picture picture = this.pictureRepository.findFirstByUrl(teamDto.getPicture().getUrl()).get();

                team.setPicture(picture);

                this.teamRepository.save(team);

                result.append(String.format(VALID_TEAM_FORMAT,
                        teamDto.getName()));

            } else {
                result.append(INVALID_TEAM).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return this.fileReader.readFile(TEAMS_XML_PATH);
    }
}
