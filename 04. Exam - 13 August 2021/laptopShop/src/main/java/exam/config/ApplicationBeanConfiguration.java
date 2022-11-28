package exam.config;

import com.google.gson.*;
import exam.model.dto.wrapper.ShopsWrapperDTO;
import exam.model.dto.wrapper.TownsWrapperDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean(name = "basicGson")
    public Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
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

    @Bean(name = "shopsContext")
    public JAXBContext createShopsContext() throws JAXBException {
        return JAXBContext.newInstance(ShopsWrapperDTO.class);
    }

    @Bean(name = "townsContext")
    public JAXBContext createTownsContext() throws JAXBException {
        return JAXBContext.newInstance(TownsWrapperDTO.class);
    }
}
