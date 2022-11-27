package com.example.football.config;

import com.example.football.models.entity.dto.wrapper.PlayersWrapperDTO;
import com.example.football.models.entity.dto.wrapper.StatsWrapperDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public Gson createGson(){
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Bean
    public ModelMapper createModelMapper(){
        return new ModelMapper();
    }

    @Bean
    public Validator createValidator(){
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    public StringBuilder createStringBuilder(){
        return new StringBuilder();
    }

    @Bean(name ="statsContext")
    public JAXBContext createStatsContext() throws JAXBException {
        return JAXBContext.newInstance(StatsWrapperDTO.class);
    }

    @Bean(name ="playersContext")
    public JAXBContext createPlayersContext() throws JAXBException {
        return JAXBContext.newInstance(PlayersWrapperDTO.class);
    }

}
