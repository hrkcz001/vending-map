package dev.morozan1.server.annotation;

import dev.morozan1.server.validator.IsDoubleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsDoubleValidator.class)
public @interface IsDouble {
    String message() default "Double is not valid";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
