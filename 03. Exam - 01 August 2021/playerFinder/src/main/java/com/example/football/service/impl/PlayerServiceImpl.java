package com.example.football.service.impl;

import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.models.entity.dto.PlayerDTO;
import com.example.football.models.entity.dto.wrapper.PlayersWrapperDTO;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.football.util.constant.FilePaths.PLAYERS_IMPORT_XML_PATH;
import static com.example.football.util.constant.OutputMessages.*;

@Service
public class PlayerServiceImpl implements PlayerService {
    
    private final PlayerRepository playerRepository;
    private final TownRepository townRepository;
    private final TeamRepository teamRepository;
    private final StatRepository statRepository;
    private final ModelMapper mapper;
    private final Validator validator;
    private final JAXBContext context;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TownRepository townRepository, TeamRepository teamRepository, StatRepository statRepository, ModelMapper mapper,
                             Validator validator, @Qualifier("playersContext") JAXBContext context) {
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
        this.teamRepository = teamRepository;
        this.statRepository = statRepository;
        this.mapper = mapper;
        this.validator = validator;
        this.context = context;
    }


    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(PLAYERS_IMPORT_XML_PATH);
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        Unmarshaller unmarshaller = context.createUnmarshaller();

        PlayersWrapperDTO playersDTO = (PlayersWrapperDTO) unmarshaller
                .unmarshal(new FileReader(PLAYERS_IMPORT_XML_PATH.toAbsolutePath().toString()));

        return playersDTO.getPlayers()
                .stream()
                .map(this::importPlayer)
                .collect(Collectors.joining("\n"));

    }

    private String importPlayer(PlayerDTO dto) {

        if (this.validator.validate(dto).isEmpty()) {
            if (this.playerRepository.findFirstByEmail(dto.getEmail()).isEmpty()) {

                Player player = mapper.map(dto, Player.class);

                Town town = this.townRepository.findByName(dto.getTown().getName()).get();
                player.setTown(town);
                Stat stat = this.statRepository.findFirstById(dto.getStat().getId());
                player.setStat(stat);
                Team team = this.teamRepository.findFirstByName(dto.getTeam().getName()).get();
                player.setTeam(team);

                this.playerRepository.save(player);



                return String.format(SUCCESSFULLY_ADDED_PLAYER_FORMAT,
                        player.getClass().getSimpleName(),
                        dto.getFirstName(),
                        dto.getLastName(),
                        dto.getPosition());
            }
        }
        return INVALID_PLAYER;
    }


    @Override
    public String exportBestPlayers() {
        LocalDate after = LocalDate.of(1995, 01, 01);
        LocalDate before = LocalDate.of(2003, 01, 01);
        List<Player> players = this.playerRepository.findBestPlayers(after, before).get();
        StringBuilder sb = new StringBuilder();

        for (Player player : players) {
            sb.append(player.toString()).append(System.lineSeparator());
        }

        return sb.toString();
    }
}
