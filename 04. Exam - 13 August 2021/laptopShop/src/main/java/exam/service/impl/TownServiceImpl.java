package exam.service.impl;

import exam.model.dto.TownDTO;
import exam.model.dto.wrapper.TownsWrapperDTO;
import exam.model.entity.Town;
import exam.repository.TownRepository;
import exam.service.TownService;
import exam.util.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static exam.util.constants.Messages.INVALID_TOWN;
import static exam.util.constants.Messages.VALID_TOWN_FORMAT;
import static exam.util.constants.Paths.TOWNS_XML_PATH;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final JAXBContext context;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, @Qualifier("townsContext") JAXBContext context, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.townRepository = townRepository;
        this.context = context;
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
        return Files.readString(Path.of(TOWNS_XML_PATH));
    }

    @Override
    public String importTowns() throws JAXBException, FileNotFoundException {
        Unmarshaller unmarshaller = this.context.createUnmarshaller();

        TownsWrapperDTO townsDto = (TownsWrapperDTO) unmarshaller
                .unmarshal(new FileReader(TOWNS_XML_PATH));


        for (TownDTO townDto : townsDto.getTowns()) {

            boolean isValid = this.validator.isValid(townDto);

            if (this.townRepository.findFirstByName(townDto.getName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Town town = this.mapper.map(townDto, Town.class);

                this.townRepository.save(town);

                result.append(String.format(VALID_TOWN_FORMAT,
                        town.getClass().getSimpleName(),
                        townDto.getName()));
                
            } else {
                result.append(INVALID_TOWN).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
