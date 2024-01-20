package dev.morozan1.server.annotation;

import dev.morozan1.server.validator.IsBooleanValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsBooleanValidator.class)
public @interface IsBoolean {
    String message() default "Boolean is not valid";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
