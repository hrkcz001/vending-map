package dev.morozan1.server.config;

import dev.morozan1.server.dto.mapper.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new CUMachineRequestDtoToMachineMapper());
        modelMapper.addConverter(new MachineToMachineResponseDtoMapper());
        modelMapper.addConverter(new CUReviewRequestDtoToReviewMapper());
        modelMapper.addConverter(new MPToMPResponseDtoMapper(modelMapper));
        modelMapper.addConverter(new CUMPRequestDtoToMPMapper());

        return modelMapper;
    }
}