package softuni.exam.service;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.PlayerDTO;
import softuni.exam.domain.dtos.PlayerExportDTO;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static softuni.exam.util.constants.Messages.INVALID_PLAYER;
import static softuni.exam.util.constants.Messages.VALID_PLAYER_FORMAT;
import static softuni.exam.util.constants.Paths.PLAYERS_JSON_PATH;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PictureRepository pictureRepository;
    private final Gson gson;
    private final ValidatorUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;
    private final FileUtil fileReader;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository, PictureRepository pictureRepository, Gson gson, ValidatorUtil validator, ModelMapper mapper, StringBuilder result, FileUtil fileReader) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.pictureRepository = pictureRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
        this.fileReader = fileReader;
    }


    @Override
    public String importPlayers() throws IOException {
        List<PlayerDTO> playersDto = Arrays.stream(gson.fromJson(readPlayersJsonFile(), PlayerDTO[].class)).toList();

        for (PlayerDTO playerDto : playersDto) {
            boolean isValid = this.validator.isValid(playerDto);

            if (this.playerRepository.findFirstByFirstNameAndLastNameAndNumber(playerDto.getFirstName(), playerDto.getLastName(), playerDto.getNumber()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Player player = this.mapper.map(playerDto, Player.class);

               Picture picture = this.pictureRepository.findFirstByUrl(playerDto.getPicture().getUrl()).get();
               Team team = this.teamRepository.findFirstByName(playerDto.getTeam().getName()).get();

                player.setPicture(picture);
                player.setTeam(team);

                this.playerRepository.save(player);

                result.append(String.format(VALID_PLAYER_FORMAT,
                        playerDto.getFirstName(),
                        playerDto.getLastName()));


            } else {
                result.append(INVALID_PLAYER).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return this.fileReader.readFile(PLAYERS_JSON_PATH);
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {

        return this.playerRepository.findPlayersBySalaryGreaterThanOrderBySalaryDesc(BigDecimal.valueOf(100000))
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .map(player -> mapper.map(player, PlayerExportDTO.class))
                .map(Object::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public String exportPlayersInATeam() {
        return this.teamRepository.findFirstByNameOrderByPlayers_IdAsc("North Hub")
                .orElseThrow(NoSuchElementException::new).getPlayers()
                .stream()
                .map(Player::toString).collect(Collectors.joining(System.lineSeparator()));
    }
}
