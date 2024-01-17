package dev.morozan1.server.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.morozan1.server.dto.mapper.CreateMachineRequestDtoToMachineMapper;

@Configuration
public class ModelMapperConfig {


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new CreateMachineRequestDtoToMachineMapper());

        return modelMapper;
    }
}