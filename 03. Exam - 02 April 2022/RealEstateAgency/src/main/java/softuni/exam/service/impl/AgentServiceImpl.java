package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.util.constants.Messages.INVALID_AGENT;
import static softuni.exam.util.constants.Messages.VALID_AGENT_FORMAT;
import static softuni.exam.util.constants.Paths.AGENTS_JSON_PATH;
import static softuni.exam.util.constants.Paths.TOWNS_JSON_PATH;

@Service
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final TownRepository townRepository;
    private final Gson gson;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository, TownRepository townRepository, Gson gson, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.agentRepository = agentRepository;
        this.townRepository = townRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }


    @Override
    public boolean areImported() {
        return this.agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of(AGENTS_JSON_PATH));
    }

    @Override
    public String importAgents() throws IOException {
        List<AgentDTO> agents = Arrays.stream(gson.fromJson(readAgentsFromFile(), AgentDTO[].class)).toList();

        for (AgentDTO agentDto : agents) {
            boolean isValid = this.validator.isValid(agentDto);

            if (this.agentRepository.findFirstByFirstNameOrEmail(agentDto.getFirstName(), agentDto.getEmail()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Agent agent = this.mapper.map(agentDto, Agent.class);

                agent.setTown(this.townRepository.findFirstByTownName(agentDto.getTown()).get());

                this.agentRepository.save(agent);

                result.append(String.format(VALID_AGENT_FORMAT,
                        agentDto.getFirstName(),
                        agentDto.getLastName()));


            } else {
                result.append(INVALID_AGENT).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
