package dev.morozan1.vendingmap.validation.validator;

import dev.morozan1.vendingmap.validation.PlaceInPrague;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.locationtech.jts.geom.Point;

import static dev.morozan1.vendingmap.entity.Machine.isInPragueValid;

public class PlaceInPragueValidator implements ConstraintValidator<PlaceInPrague, Point> {

    @Override
    public void initialize(PlaceInPrague parameters) {}

    @Override
    public boolean isValid(Point coordinate, ConstraintValidatorContext context) {
        if (coordinate == null) {
            return false;
        }

        return isInPragueValid(coordinate);
    }
}