package dev.morozan1.server.annotation;

import dev.morozan1.server.validator.IsMachineIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsMachineIdValidator.class)
public @interface IsMachineId {
    String message() default "Should be Long representing Machine's Id";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
