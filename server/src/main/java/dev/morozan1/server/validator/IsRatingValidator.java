package dev.morozan1.server.validator;

import dev.morozan1.server.annotation.IsRating;
import dev.morozan1.server.dto.TimePeriodDto;
import dev.morozan1.server.annotation.IsOptionalTimePeriod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.sql.Time;

public class IsRatingValidator implements ConstraintValidator<IsRating, String> {

    @Override
    public void initialize(IsRating constraintAnnotation) {
    }

    @Override
    public boolean isValid(String ratingString, ConstraintValidatorContext constraintValidatorContext) {
        if (ratingString == null) return false;

        try {
            int rating = Short.parseShort(ratingString);
            return rating >= 1 && rating <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
