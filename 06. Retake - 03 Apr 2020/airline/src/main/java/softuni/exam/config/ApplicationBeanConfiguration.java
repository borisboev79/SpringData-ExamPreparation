package softuni.exam.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.exam.models.dto.wrapper.PlanesWrapperDTO;
import softuni.exam.models.dto.wrapper.TicketsWrapperDTO;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;


@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
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

    @Bean(name = "planesContext")
    public JAXBContext createPlanesContext() throws JAXBException {
        return JAXBContext.newInstance(PlanesWrapperDTO.class);
    }

    @Bean(name = "ticketsContext")
    public JAXBContext createTicketsContext() throws JAXBException {
        return JAXBContext.newInstance(TicketsWrapperDTO.class);
    }
}
