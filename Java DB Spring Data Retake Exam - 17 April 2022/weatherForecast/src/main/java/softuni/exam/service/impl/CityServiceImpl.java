package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CityImportDTO;
import softuni.exam.models.entity.City;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_CITY;
import static softuni.exam.constants.Messages.VALID_CITY_FORMAT;
import static softuni.exam.constants.Paths.CITIES_JSON_PATH;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final StringBuilder result;
    private final ValidationUtils validationUtils;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository, ModelMapper modelMapper, Gson gson, StringBuilder result, ValidationUtils validationUtils) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.result = result;
        this.validationUtils = validationUtils;
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(Path.of(CITIES_JSON_PATH));
    }

    @Override
    public String importCities() throws IOException {


        List<CityImportDTO> cities = Arrays.stream(gson.fromJson(readCitiesFileContent(), CityImportDTO[].class)).toList();

        for (CityImportDTO cityDto : cities) {
            boolean isValid = this.validationUtils.isValid(cityDto);

            if (this.cityRepository.findFirstByCityName(cityDto.getCityName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                City city = this.modelMapper.map(cityDto, City.class);

                    city.setCountry(this.countryRepository.findById(cityDto.getCountry()).get());

                    this.cityRepository.save(city);

                    result.append(String.format(VALID_CITY_FORMAT,
                            cityDto.getCityName(),
                            cityDto.getPopulation()));


            } else {
                result.append(INVALID_CITY).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
