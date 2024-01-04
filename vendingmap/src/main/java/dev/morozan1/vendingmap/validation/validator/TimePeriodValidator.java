package dev.morozan1.vendingmap.validation.validator;

import dev.morozan1.vendingmap.validation.TimePeriod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static dev.morozan1.vendingmap.entity.Machine.isTimePeriodValid;

public class TimePeriodValidator implements ConstraintValidator<TimePeriod, String> {

    public void initialize(TimePeriod parameters) {}

    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        return isTimePeriodValid(value);
    }
}