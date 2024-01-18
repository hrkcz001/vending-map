package dev.morozan1.server.validation;

import dev.morozan1.server.validation.validator.IsOptionalTimePeriodValidator;
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
@Constraint(validatedBy = IsOptionalTimePeriodValidator.class)
public @interface IsOptionalTimePeriod {
    String message() default "Time period is not valid";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
