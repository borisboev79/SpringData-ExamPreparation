package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PassengerDTO;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static softuni.exam.util.constants.Messages.INVALID_MESSAGE_FORMAT;
import static softuni.exam.util.constants.Messages.VALID_PASSENGER_FORMAT;
import static softuni.exam.util.constants.Paths.PASSENGERS_JSON_PATH;

@Service
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final TownRepository townRepository;
    private final Gson gson;
    private final ValidationUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository, TownRepository townRepository, Gson gson, ValidationUtil validator, ModelMapper mapper, StringBuilder result) {
        this.passengerRepository = passengerRepository;
        this.townRepository = townRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGERS_JSON_PATH));
    }

    @Override
    public String importPassengers() throws IOException {

        List<PassengerDTO> passengersDto = Arrays.stream(gson.fromJson(readPassengersFileContent(), PassengerDTO[].class)).toList();

        for (PassengerDTO passengerDto : passengersDto) {
            boolean isValid = this.validator.isValid(passengerDto);

            if (this.passengerRepository.findFirstByEmail(passengerDto.getEmail()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Passenger passenger = this.mapper.map(passengerDto, Passenger.class);


                Town town = this.townRepository.findFirstByName(passengerDto.getTown()).get();

                passenger.setTown(town);

                this.passengerRepository.save(passenger);

                result.append(String.format(VALID_PASSENGER_FORMAT,
                        passenger.getClass().getSimpleName(),
                        passengerDto.getLastName(),
                        passengerDto.getEmail()));


            } else {
                result.append(String.format(INVALID_MESSAGE_FORMAT, "Passenger")).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        return this.passengerRepository
                .findAllByOrderByTicketsCountDescEmailAsc()
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .map(Passenger::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
