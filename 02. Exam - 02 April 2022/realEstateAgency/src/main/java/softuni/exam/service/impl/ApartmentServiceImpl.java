package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentDTO;
import softuni.exam.models.dto.wrapper.ApartmentsWrapperDTO;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.constants.ApartmentType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.util.constants.Messages.INVALID_APARTMENT;
import static softuni.exam.util.constants.Messages.VALID_APARTMENT_FORMAT;
import static softuni.exam.util.constants.Paths.APARTMENTS_XML_PATH;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final TownRepository townRepository;
    private final JAXBContext context;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, TownRepository townRepository, @Qualifier("apartmentsContext") JAXBContext context, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.apartmentRepository = apartmentRepository;
        this.townRepository = townRepository;
        this.context = context;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of(APARTMENTS_XML_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        Unmarshaller unmarshaller = this.context.createUnmarshaller();

        ApartmentsWrapperDTO apartmentsDto = (ApartmentsWrapperDTO) unmarshaller
                .unmarshal(new FileReader(APARTMENTS_XML_PATH));


        for (ApartmentDTO apartmentDto : apartmentsDto.getApartments()) {

            boolean isValid = this.validator.isValid(apartmentDto);

            final Town town = this.townRepository.findFirstByTownName(apartmentDto.getTown()).get();

            if (this.apartmentRepository.findFirstByAreaAndTown_TownName(apartmentDto.getArea(),
                    apartmentDto.getTown()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Apartment apartment = this.mapper.map(apartmentDto, Apartment.class);

                apartment.setTown(town);

                this.apartmentRepository.save(apartment);

                result.append(String.format(VALID_APARTMENT_FORMAT,
                        apartmentDto.getApartmentType(),
                        apartmentDto.getArea()));


            } else {
                result.append(INVALID_APARTMENT).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
