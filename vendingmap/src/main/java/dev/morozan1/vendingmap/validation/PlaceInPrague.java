package dev.morozan1.vendingmap.validation;

import dev.morozan1.vendingmap.validation.validator.PlaceInPragueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( {ElementType.FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = PlaceInPragueValidator.class)
public @interface PlaceInPrague {
    String message() default "Coordinates should be in Prague";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}