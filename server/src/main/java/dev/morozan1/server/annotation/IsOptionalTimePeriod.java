package dev.morozan1.server.annotation;

import dev.morozan1.server.validator.IsOptionalTimePeriodValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsOptionalTimePeriodValidator.class)
public @interface IsOptionalTimePeriod {
    String message() default "Time period is not valid";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
