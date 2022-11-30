package softuni.exam.config;

import com.google.gson.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.exam.models.dto.wrapper.OffersWrapperDTO;
import softuni.exam.models.dto.wrapper.SellersWrapperDTO;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Configuration
public class ApplicationBeanConfiguration {

    @Bean(name = "dateTimeConverter")
    public static Gson createGsonWithDatesAndTimes() {

        final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        JsonDeserializer<LocalDateTime> deserializer = (json, t, c) -> LocalDateTime.parse(json.getAsString(), dateFormat);

        JsonSerializer<String> serializer = (date, t, c) -> new JsonPrimitive(date);

        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, deserializer)
                .registerTypeAdapter(LocalDateTime.class, serializer)
                .create();
    }
    @Bean(name = "dateConverter")
    public static Gson createGsonWithDates() {

        final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        JsonDeserializer<LocalDate> deserializer = (json, t, c) -> LocalDate.parse(json.getAsString(), dateFormat);

        JsonSerializer<String> serializer = (date, t, c) -> new JsonPrimitive(date);

        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, deserializer)
                .registerTypeAdapter(LocalDate.class, serializer)
                .create();
    }

    @Bean
    public ModelMapper createModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Validator createValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    public StringBuilder createStringBuilder() {
        return new StringBuilder();
    }

    @Bean(name = "offersContext")
    public JAXBContext createOffersContext() throws JAXBException {
        return JAXBContext.newInstance(OffersWrapperDTO.class);
    }

    @Bean(name = "sellersContext")
    public JAXBContext createSellersContext() throws JAXBException {
        return JAXBContext.newInstance(SellersWrapperDTO.class);
    }
}
