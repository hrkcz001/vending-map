package dev.morozan1.server.config;

import dev.morozan1.server.dto.mapper.MachineToMachineResponseDtoMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.morozan1.server.dto.mapper.CUMachineRequestDtoToMachineMapper;

@Configuration
public class ModelMapperConfig {


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new CUMachineRequestDtoToMachineMapper());
        modelMapper.addConverter(new MachineToMachineResponseDtoMapper());

        return modelMapper;
    }
}