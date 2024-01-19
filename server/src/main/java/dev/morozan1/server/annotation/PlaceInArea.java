package dev.morozan1.server.annotation;

import dev.morozan1.server.validator.PlaceInAreaValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PlaceInAreaValidator.class)
public @interface PlaceInArea {
    String message() default "Coordinates must be in allowed area";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
