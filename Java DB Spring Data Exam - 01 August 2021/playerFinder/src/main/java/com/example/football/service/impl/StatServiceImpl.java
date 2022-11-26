package com.example.football.service.impl;

import com.example.football.models.entity.Stat;
import com.example.football.models.entity.dto.StatDTO;
import com.example.football.models.entity.dto.wrapper.StatsWrapperDTO;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

import static com.example.football.util.constant.FilePaths.STATS_IMPORT_XML_PATH;
import static com.example.football.util.constant.OutputMessages.*;

@Service
public class StatServiceImpl implements StatService {


    private final StatRepository statRepository;
    private final ModelMapper mapper;
    private final Validator validator;
    private final JAXBContext context;

    public StatServiceImpl(StatRepository statRepository, ModelMapper mapper,
                           Validator validator, @Qualifier("statsContext") JAXBContext context) {
        this.statRepository = statRepository;
        this.mapper = mapper;
        this.validator = validator;
        this.context = context;
    }

    @Override
    public boolean areImported() {
        return this.statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(STATS_IMPORT_XML_PATH);
    }

    @Override
    public String importStats() throws JAXBException, IOException {

        Unmarshaller unmarshaller = context.createUnmarshaller();

        StatsWrapperDTO statsDTO = (StatsWrapperDTO) unmarshaller
                .unmarshal(new FileReader(STATS_IMPORT_XML_PATH.toAbsolutePath().toString()));

        return statsDTO.getStats()
                .stream()
                .map(this::importStat)
                .collect(Collectors.joining("\n"));

    }

    private String importStat(StatDTO dto) {

            if (this.validator.validate(dto).isEmpty()) {
                if (this.statRepository.findByShootingAndPassingAndEndurance(dto.getShooting(),
                        dto.getPassing(),
                        dto.getEndurance()).isEmpty()) {

                    Stat stat = mapper.map(dto, Stat.class);
                    this.statRepository.save(stat);

                    return String.format(SUCCESSFULLY_ADDED_STAT_FORMAT,
                            stat.getClass().getSimpleName(),
                            dto.getPassing(),
                            dto.getShooting(),
                            dto.getEndurance());
                }
                return INVALID_STAT;
            } else {
                return INVALID_STAT;
            }
        }
    }
