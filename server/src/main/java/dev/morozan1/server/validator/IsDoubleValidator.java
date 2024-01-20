package dev.morozan1.server.validator;

import dev.morozan1.server.annotation.IsDouble;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsDoubleValidator implements ConstraintValidator<IsDouble, String> {

    @Override
    public void initialize(IsDouble constraintAnnotation) {
    }

    @Override
    public boolean isValid(String doubleString, ConstraintValidatorContext constraintValidatorContext) {
        if (doubleString == null) return false;

        try {
            Double.parseDouble(doubleString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
