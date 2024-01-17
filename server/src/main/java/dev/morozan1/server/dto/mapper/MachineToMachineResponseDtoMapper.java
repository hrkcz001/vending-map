package dev.morozan1.server.dto.mapper;

import dev.morozan1.server.dto.CoordinatesDto;
import dev.morozan1.server.dto.MachineResponseDto;
import dev.morozan1.server.dto.TimePeriodDto;
import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.entity.MachineProduct;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;

public class MachineToMachineResponseDtoMapper {
    public static MachineResponseDto convertToDto(Machine machine, ModelMapper modelMapper) {
        MachineResponseDto machineResponseDto = modelMapper.map(machine, MachineResponseDto.class);

        machineResponseDto.setRating(machine.calculateRating(machine.getReviews()));
        machineResponseDto.setReviewsCount(machine.getReviews().size());
        CoordinatesDto coordinatesDto = new CoordinatesDto();
        coordinatesDto.setLatitude(String.valueOf(machine.getLatitude()));
        coordinatesDto.setLongitude(String.valueOf(machine.getLongitude()));
        machineResponseDto.setCoordinates(coordinatesDto);
        if (machine.getAvailableTo() != null && machine.getAvailableFrom() != null) {
            TimePeriodDto timePeriodDto = new TimePeriodDto();
            timePeriodDto.setAvailableFrom(machine.getAvailableFrom().toString().substring(0, 5));
            timePeriodDto.setAvailableTo(machine.getAvailableTo().toString().substring(0, 5));
            machineResponseDto.setAvailableTime(timePeriodDto);
        }
        machineResponseDto.setProducts(productAvailabilityList(machine.getMachineProducts()));

        return machineResponseDto;
    }


    private static List<MachineResponseDto.ProductAvailabilityDto> productAvailabilityList(Set<MachineProduct> machineProductSet) {
        return machineProductSet.stream()
                .map(entry -> {
                    MachineResponseDto.ProductAvailabilityDto productAvailabilityDto = new MachineResponseDto.ProductAvailabilityDto();
                    productAvailabilityDto.setProductId(entry.getProduct().getProductId());
                    productAvailabilityDto.setAvailability(entry.getAvailability());
                    return productAvailabilityDto;
                })
                .toList();
    }

}