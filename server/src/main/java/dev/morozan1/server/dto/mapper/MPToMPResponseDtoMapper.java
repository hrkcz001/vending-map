package dev.morozan1.server.dto.mapper;

import dev.morozan1.server.dto.MachineProductResponseDto;
import dev.morozan1.server.dto.ProductResponseDto;
import dev.morozan1.server.entity.MachineProduct;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MPToMPResponseDtoMapper extends AbstractConverter<MachineProduct, MachineProductResponseDto> {

    private final ModelMapper modelMapper;

    public MPToMPResponseDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    protected MachineProductResponseDto convert(MachineProduct machineProduct) {
        MachineProductResponseDto machineProductResponseDto = new MachineProductResponseDto();

        machineProductResponseDto.setPrice(machineProduct.getPrice());
        machineProductResponseDto.setIsAvailable(machineProduct.getAvailability());

        ProductResponseDto productResponseDto = modelMapper.map(machineProduct.getProduct(), ProductResponseDto.class);
        machineProductResponseDto.setProduct(productResponseDto);

        return machineProductResponseDto;
    }
}
