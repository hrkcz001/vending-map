package dev.morozan1.server.validator;

import dev.morozan1.server.dto.TimePeriodDto;
import dev.morozan1.server.annotation.IsOptionalTimePeriod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.sql.Time;

public class IsOptionalTimePeriodValidator implements ConstraintValidator<IsOptionalTimePeriod, TimePeriodDto> {

    @Override
    public void initialize(IsOptionalTimePeriod constraintAnnotation) {
    }

    @Override
    public boolean isValid(TimePeriodDto timePeriod, ConstraintValidatorContext constraintValidatorContext) {
        if (timePeriod == null) return true;

        String startTimeString = timePeriod.getAvailableFrom();
        String endTimeString = timePeriod.getAvailableTo();

        if (startTimeString == null || endTimeString == null) {
            return false;
        }

        if (!startTimeString.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]") ||
                !endTimeString.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            return false;
        }

        Time start = Time.valueOf(startTimeString + ":00");
        Time end = Time.valueOf(endTimeString + ":00");

        return start.before(end);
    }
}
