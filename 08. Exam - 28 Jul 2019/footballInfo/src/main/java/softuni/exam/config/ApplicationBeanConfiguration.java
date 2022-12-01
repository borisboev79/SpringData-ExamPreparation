package softuni.exam.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.exam.domain.dtos.wrappers.PicturesWrapperDTO;
import softuni.exam.domain.dtos.wrappers.TeamsWrapperDTO;
import softuni.exam.util.FileUtil;
import softuni.exam.util.FileUtilImpl;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.ValidatorUtilImpl;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public FileUtil fileUtil() {
        return new FileUtilImpl();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    }

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    public ValidatorUtil validationUtil() {
        return new ValidatorUtilImpl(validator());
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean(name = "picturesContext")
    public JAXBContext createPicturesContext() throws JAXBException {
        return JAXBContext.newInstance(PicturesWrapperDTO.class);
    }

    @Bean(name = "teamsContext")
    public JAXBContext createTeamsContext() throws JAXBException {
        return JAXBContext.newInstance(TeamsWrapperDTO.class);
    }

    @Bean
    public StringBuilder createStringBuilder() {
        return new StringBuilder();
    }


}
