package dev.morozan1.server.validator;

import dev.morozan1.server.annotation.IsBoolean;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsBooleanValidator implements ConstraintValidator<IsBoolean, String> {

    @Override
    public void initialize(IsBoolean constraintAnnotation) {
    }

    @Override
    public boolean isValid(String booleanString, ConstraintValidatorContext constraintValidatorContext) {
        if (booleanString == null) return false;
        return  booleanString.equalsIgnoreCase("true") ||
                booleanString.equalsIgnoreCase("false");
    }
}
