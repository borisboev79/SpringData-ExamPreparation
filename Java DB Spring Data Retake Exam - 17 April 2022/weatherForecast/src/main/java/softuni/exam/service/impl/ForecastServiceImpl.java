package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastImportDTO;
import softuni.exam.models.dto.ForecastsWrapperDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Forecast;
import softuni.exam.models.entity.enums.DaysOfWeek;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static softuni.exam.constants.Messages.*;
import static softuni.exam.constants.Paths.FORECASTS_XML_PATH;

@Service
public class ForecastServiceImpl implements ForecastService {
    private final ForecastRepository forecastRepository;
    private final CityRepository cityRepository;
    private final JAXBContext context;
    private final ValidationUtils validator;
    private final ModelMapper modelMapper;
    private final StringBuilder result;


    @Autowired
    public ForecastServiceImpl(ForecastRepository forecastRepository, CityRepository cityRepository, JAXBContext context, ValidationUtils validator, ModelMapper modelMapper, StringBuilder result) {
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;
        this.context = context;
        this.validator = validator;
        this.modelMapper = modelMapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(Path.of(FORECASTS_XML_PATH));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        Unmarshaller unmarshaller = this.context.createUnmarshaller();

        ForecastsWrapperDTO forecastDTO = (ForecastsWrapperDTO) unmarshaller
                .unmarshal(new FileReader(FORECASTS_XML_PATH));


        for (ForecastImportDTO forecastDto : forecastDTO.getForecasts()) {

            boolean isValid = this.validator.isValid(forecastDto);

            final City city = this.cityRepository.getById(forecastDto.getCity());

            if (this.forecastRepository.findFirstByDayOfWeekAndCity(forecastDto.getDayOfWeek(),
                   city).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Forecast forecast = this.modelMapper.map(forecastDto, Forecast.class);

                forecast.setCity(city);

                this.forecastRepository.save(forecast);

                result.append(String.format(VALID_FORECAST_FORMAT,
                        forecastDto.getDayOfWeek(),
                        forecastDto.getMaxTemperature()));


            } else {
                result.append(INVALID_FORECAST).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public String exportForecasts() {

        Set<Forecast> forecasts = this.forecastRepository.findAllByDayOfWeekAndCity_PopulationLessThanOrderByMaxTemperatureDescIdAsc(DaysOfWeek.SUNDAY, 150000).get();

        for (Forecast forecast : forecasts) {
            result.append(forecast.toString()).append(System.lineSeparator());
        }

        return result.toString();
    }


}
