package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CarDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static softuni.exam.util.constants.Messages.INVALID_CAR;
import static softuni.exam.util.constants.Messages.VALID_CAR_FORMAT;
import static softuni.exam.util.constants.Paths.CARS_JSON_PATH;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final Gson gson;
    private final ValidationUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, @Qualifier("dateConverter") Gson gson, ValidationUtil validator, ModelMapper mapper, StringBuilder result) {
        this.carRepository = carRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }


    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(Path.of(CARS_JSON_PATH));
    }

    @Override
    public String importCars() throws IOException {

        List<CarDTO> carsDto = Arrays.stream(gson.fromJson(readCarsFileContent(), CarDTO[].class)).toList();

        for (CarDTO carDto : carsDto) {
            boolean isValid = this.validator.isValid(carDto);

            if (this.carRepository.findFirstByMakeAndModelAndKilometers(carDto.getMake(), carDto.getModel(), carDto.getKilometers()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Car car = this.mapper.map(carDto, Car.class);

                this.carRepository.save(car);

                result.append(String.format(VALID_CAR_FORMAT,
                        carDto.getMake(),
                        carDto.getModel()));


            } else {
                result.append(INVALID_CAR).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        return this.carRepository
                .findAllCarsOrderByPictureCountDescMakeAsc()
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .map(Car::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
