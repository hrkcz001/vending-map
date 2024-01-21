package dev.morozan1.server.dto.mapper;

import dev.morozan1.server.dto.CoordinatesDto;
import dev.morozan1.server.dto.response.MachineResponseDto;
import dev.morozan1.server.dto.TimePeriodDto;
import dev.morozan1.server.entity.Machine;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

@Component
public class MachineToMachineResponseDtoMapper extends AbstractConverter<Machine, MachineResponseDto> {

    @Override
    protected MachineResponseDto convert(Machine machine) {
        MachineResponseDto machineResponseDto = new MachineResponseDto();

        machineResponseDto.setMachineId(machine.getMachineId());

        //Escape HTML
        machineResponseDto.setAddress(HtmlUtils.htmlEscape(machine.getAddress()));
        machineResponseDto.setDescription(HtmlUtils.htmlEscape(machine.getDescription()));

        machineResponseDto.setRating(machine.calculateRating(machine.getReviews()));
        machineResponseDto.setReviewsCount(machine.getReviews().size());
        CoordinatesDto coordinatesDto = new CoordinatesDto();
        coordinatesDto.setLatitude(machine.getLatitude());
        coordinatesDto.setLongitude(machine.getLongitude());
        machineResponseDto.setCoordinates(coordinatesDto);
        if (machine.getAvailableTo() != null && machine.getAvailableFrom() != null) {
            TimePeriodDto timePeriodDto = new TimePeriodDto();
            timePeriodDto.setAvailableFrom(machine.getAvailableFrom().toString().substring(0, 5));
            timePeriodDto.setAvailableTo(machine.getAvailableTo().toString().substring(0, 5));
            machineResponseDto.setAvailableTime(timePeriodDto);
        }

        return machineResponseDto;
    }
}