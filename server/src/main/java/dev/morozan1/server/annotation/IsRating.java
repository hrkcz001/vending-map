package dev.morozan1.server.annotation;

import dev.morozan1.server.validator.IsRatingValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsRatingValidator.class)
public @interface IsRating {
    String message() default "Should be number between 1 and 5";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
