package dev.morozan1.server.util;

import dev.morozan1.server.dto.CoordinatesDto;
import dev.morozan1.server.dto.CreateMachineRequestDto;
import dev.morozan1.server.dto.TimePeriodDto;
import dev.morozan1.server.entity.Machine;
import org.modelmapper.AbstractConverter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.sql.Time;

@Component
public class CreateMachineRequestDtoToMachineConverter extends AbstractConverter<CreateMachineRequestDto, Machine> {

    @Override
    protected Machine convert(CreateMachineRequestDto source) {

        Machine machine = new Machine();
        machine.setAddress(source.getAddress());
        machine.setDescription(source.getDescription());

        CoordinatesDto coordinatesDto = source.getCoordinates();
        if (coordinatesDto != null) {
            Double latitude = Double.parseDouble(coordinatesDto.getLatitude());
            Double longitude = Double.parseDouble(coordinatesDto.getLongitude());
            Pair<Double, Double> coordinates = Pair.of(latitude, longitude);
            machine.setCoordinates(coordinates);
        }

        TimePeriodDto timePeriodDto = source.getAvailableTime();
        if (timePeriodDto != null) {
            Time availableFrom = Time.valueOf(timePeriodDto.getAvailableFrom() + ":00");
            Time availableTo = Time.valueOf(timePeriodDto.getAvailableTo() + ":00");
            Pair<Time, Time> availableTime = Pair.of(availableFrom, availableTo);
            machine.setAvailableTime(availableTime);
        }

        return machine;
    }

}