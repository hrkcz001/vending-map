package dev.morozan1.server.config;

import dev.morozan1.server.dto.mapper.CreateReviewRequestDtoToReviewMapper;
import dev.morozan1.server.dto.mapper.MachineToMachineResponseDtoMapper;
import dev.morozan1.server.dto.mapper.UpdateReviewRequestDtoToReviewMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.morozan1.server.dto.mapper.CUMachineRequestDtoToMachineMapper;
import dev.morozan1.server.repository.MachineRepository;

@Configuration
public class ModelMapperConfig {

    private final MachineRepository machineRepository;

    @Autowired
    public ModelMapperConfig(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new CUMachineRequestDtoToMachineMapper());
        modelMapper.addConverter(new MachineToMachineResponseDtoMapper());
        modelMapper.addConverter(new CreateReviewRequestDtoToReviewMapper(machineRepository));
        modelMapper.addConverter(new UpdateReviewRequestDtoToReviewMapper());

        return modelMapper;
    }
}