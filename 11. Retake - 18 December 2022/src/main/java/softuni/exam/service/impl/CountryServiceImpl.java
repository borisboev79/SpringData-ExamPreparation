package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountryDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.constants.Messages.*;
import static softuni.exam.constants.Paths.COUNTRIES_JSON_PATH;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final Gson gson;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, Gson gson, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.countryRepository = countryRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFileContent() throws IOException {
        return Files.readString(Path.of(COUNTRIES_JSON_PATH));
    }

    @Override
    public String importCountries() throws IOException {
        List<CountryDTO> countries = Arrays.stream(gson.fromJson(readCountriesFileContent(), CountryDTO[].class)).toList();

        for (CountryDTO countryDto : countries) {
            boolean isValid = this.validator.isValid(countryDto);

            if (this.countryRepository.findFirstByName(countryDto.getName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Country country = this.mapper.map(countryDto, Country.class);

                this.countryRepository.save(country);

                result.append(String.format(VALID_COUNTRY_FORMAT,
                        countryDto.getName(),
                        countryDto.getCountryCode()));


            } else {
                result.append(INVALID_COUNTRY).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
