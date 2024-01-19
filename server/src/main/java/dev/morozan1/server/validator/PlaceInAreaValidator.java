package dev.morozan1.server.validator;

import dev.morozan1.server.dto.CoordinatesDto;
import dev.morozan1.server.annotation.PlaceInArea;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static dev.morozan1.server.utils.AreaUtils.isInArea;

public class PlaceInAreaValidator implements ConstraintValidator<PlaceInArea, CoordinatesDto> {

    @Override
    public void initialize(PlaceInArea constraintAnnotation) {
    }

    @Override
    public boolean isValid(CoordinatesDto coordinates, ConstraintValidatorContext constraintValidatorContext) {
        if (coordinates == null) {
            return false;
        }

        double latitude = Double.parseDouble(coordinates.getLatitude());
        double longitude = Double.parseDouble(coordinates.getLongitude());

        return isInArea(latitude, longitude);
    }
}
