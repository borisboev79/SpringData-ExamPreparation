package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PlaneDTO;
import softuni.exam.models.dto.wrapper.PlanesWrapperDTO;
import softuni.exam.models.entity.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.util.constants.Messages.INVALID_MESSAGE_FORMAT;
import static softuni.exam.util.constants.Messages.VALID_PLANE_FORMAT;
import static softuni.exam.util.constants.Paths.PLANES_XML_PATH;

@Service
public class PlaneServiceImpl implements PlaneService {
    private final PlaneRepository planeRepository;
    private final JAXBContext context;
    private final ValidationUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public PlaneServiceImpl(PlaneRepository planeRepository, @Qualifier("planesContext") JAXBContext context, ValidationUtil validator, ModelMapper mapper, StringBuilder result) {
        this.planeRepository = planeRepository;
        this.context = context;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(PLANES_XML_PATH));
    }

    @Override
    public String importPlanes() throws JAXBException, FileNotFoundException {
        Unmarshaller unmarshaller = this.context.createUnmarshaller();

        PlanesWrapperDTO planesDto = (PlanesWrapperDTO) unmarshaller
                .unmarshal(new FileReader(PLANES_XML_PATH));


        for (PlaneDTO planeDto : planesDto.getPlanes()) {

            boolean isValid = this.validator.isValid(planeDto);

            if (this.planeRepository.findFirstByRegisterNumber(planeDto.getRegisterNumber()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Plane plane = this.mapper.map(planeDto, Plane.class);

                this.planeRepository.save(plane);

                result.append(String.format(VALID_PLANE_FORMAT,
                        plane.getClass().getSimpleName(),
                        planeDto.getRegisterNumber()));

            } else {
                result.append(String.format(INVALID_MESSAGE_FORMAT, "Plane")).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
