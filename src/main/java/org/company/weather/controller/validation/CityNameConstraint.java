package org.company.weather.controller.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CityParameterValidator.class)
@Target( { METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface CityNameConstraint {

    String message() default "Invalid city name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}