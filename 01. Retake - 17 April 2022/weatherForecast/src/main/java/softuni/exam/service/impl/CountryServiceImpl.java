package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountryImportDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.exam.constants.Messages.INVALID_COUNTRY;
import static softuni.exam.constants.Messages.VALID_COUNTRY_FORMAT;
import static softuni.exam.constants.Paths.COUNTRIES_JSON_PATH;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final Gson gson;
    private final ValidationUtils validationUtils;
    private final ModelMapper modelMapper;
    private final StringBuilder result;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, Gson gson, ValidationUtils validationUtils, ModelMapper modelMapper, StringBuilder result) {
        this.countryRepository = countryRepository;
        this.gson = gson;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(Path.of(COUNTRIES_JSON_PATH));
    }

    @Override
    public String importCountries() throws IOException {

        final List<CountryImportDTO> countries = Arrays.stream(gson.fromJson(readCountriesFromFile(), CountryImportDTO[].class)).toList();

        for (CountryImportDTO countryDto : countries) {

            boolean isValid = this.validationUtils.isValid(countryDto);


            if (this.countryRepository.findFirstByCountryName(countryDto.getCountryName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                this.countryRepository.save(this.modelMapper.map(countryDto, Country.class));

                result.append(String.format(VALID_COUNTRY_FORMAT,
                        countryDto.getCountryName(),
                        countryDto.getCurrency()));

            } else {
                result.append(INVALID_COUNTRY).append(System.lineSeparator());
            }

        }
        return result.toString();
    }

}
