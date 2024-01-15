package dev.morozan1.server.validation.validator;

import dev.morozan1.server.dto.CoordinatesDto;
import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.validation.PlaceInPrague;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PlaceInPragueValidator implements ConstraintValidator<PlaceInPrague, CoordinatesDto> {

    @Override
    public boolean isValid(CoordinatesDto coordinates, ConstraintValidatorContext constraintValidatorContext) {
        if (coordinates == null) {
            return false;
        }

        double latitude = Double.parseDouble(coordinates.getLatitude());
        double longitude = Double.parseDouble(coordinates.getLongitude());

        return Machine.isInPrague(latitude, longitude);
    }
}
