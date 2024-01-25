package dev.morozan1.server.dto.mapper;

import dev.morozan1.server.dto.CoordinatesDto;
import dev.morozan1.server.dto.request.CUMachineRequestDto;
import dev.morozan1.server.dto.TimePeriodDto;
import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.exception.BadRequestException;
import org.modelmapper.AbstractConverter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.sql.Time;

@Component
public class CUMachineRequestDtoToMachineMapper extends AbstractConverter<CUMachineRequestDto, Machine> {

    @Override
    protected Machine convert(CUMachineRequestDto source) {
        try {
            Machine machine = new Machine();

            //Escape HTML
            machine.setAddress(HtmlUtils.htmlEscape(source.getAddress()));
            if (source.getDescription() != null)
                machine.setDescription(HtmlUtils.htmlEscape(source.getDescription()));

            CoordinatesDto coordinatesDto = source.getCoordinates();
            Pair<Double, Double> coordinates = Pair.of(coordinatesDto.getLatitude(), coordinatesDto.getLongitude());
            machine.setCoordinates(coordinates);

            TimePeriodDto timePeriodDto = source.getAvailableTime();
            if (timePeriodDto != null) {
                Time availableFrom = Time.valueOf(timePeriodDto.getAvailableFrom() + ":00");
                Time availableTo = Time.valueOf(timePeriodDto.getAvailableTo() + ":00");
                Pair<Time, Time> availableTime = Pair.of(availableFrom, availableTo);
                machine.setAvailableTime(availableTime);
            }

            return machine;
        } catch (Exception e) {
            throw new BadRequestException("availableFrom, availableTo should be time in format HH:MM");
        }
    }
}