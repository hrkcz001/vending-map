package dev.morozan1.vendingmap.validation;

import dev.morozan1.vendingmap.validation.validator.TimePeriodValidator;
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
@Constraint(validatedBy = TimePeriodValidator.class)
public @interface TimePeriod {
    String message() default "Should be in format HH:MM-HH:MM";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}