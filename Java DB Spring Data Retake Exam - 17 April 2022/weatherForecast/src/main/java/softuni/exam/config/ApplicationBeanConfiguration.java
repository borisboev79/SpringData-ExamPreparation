package softuni.exam.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.exam.models.dto.ForecastsWrapperDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Configuration
public class ApplicationBeanConfiguration {


    @Bean
    public Gson gson(){
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public StringBuilder createStringBuilder(){
        return new StringBuilder();
    }

    @Bean
    public JAXBContext createForecastContext() throws JAXBException {
        return JAXBContext.newInstance(ForecastsWrapperDTO.class);
    }
}
