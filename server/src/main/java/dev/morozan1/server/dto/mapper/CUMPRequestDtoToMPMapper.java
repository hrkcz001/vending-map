package dev.morozan1.server.dto.mapper;

import dev.morozan1.server.dto.request.CUMachineProductRequestDto;
import dev.morozan1.server.entity.MachineProduct;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class CUMPRequestDtoToMPMapper extends AbstractConverter<CUMachineProductRequestDto, MachineProduct> {

    @Override
    public MachineProduct convert(CUMachineProductRequestDto source) {
        MachineProduct machineProduct = new MachineProduct();
        machineProduct.setAvailability(source.getIsAvailable());
        machineProduct.setPrice(source.getPrice());
        return machineProduct;
    }
}
