package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CarDTO;
import softuni.exam.models.dto.wrapper.CarsWrapperDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.constants.Messages.INVALID_CAR;
import static softuni.exam.constants.Messages.VALID_CAR_FORMAT;
import static softuni.exam.constants.Paths.CARS_XML_PATH;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final XmlParser xmlParser;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, XmlParser xmlParser,
                          ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.carRepository = carRepository;
        this.xmlParser = xmlParser;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return Files.readString(Path.of(CARS_XML_PATH));
    }

    @Override
    public String importCars() throws IOException, JAXBException {

        CarsWrapperDTO cars = xmlParser.parseXml(CarsWrapperDTO.class, CARS_XML_PATH);

        for (CarDTO carDto : cars.getCars()) {

            boolean isValid = this.validator.isValid(carDto);

            if (this.carRepository.findFirstByPlateNumber(carDto.getPlateNumber()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Car car = this.mapper.map(carDto, Car.class);

                this.carRepository.save(car);

                result.append(String.format(VALID_CAR_FORMAT,
                        carDto.getCarMake(),
                        carDto.getCarModel()));

            } else {
                result.append(INVALID_CAR).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
