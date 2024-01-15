package dev.morozan1.server.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.morozan1.server.util.CreateMachineRequestDtoToMachineConverter;

@Configuration
public class ModelMapperConfig {


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new CreateMachineRequestDtoToMachineConverter());

        return modelMapper;
    }
}