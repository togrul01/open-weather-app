package org.company.weather.controller.validation;

import org.apache.commons.lang3.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class CityParameterValidator implements ConstraintValidator<CityNameConstraint, String> {
    @Override
    public void initialize(CityNameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        value = value.replaceAll("[^a-zA-Z0-9]", "");
        boolean isValid = !StringUtils.isNumeric(value) && !StringUtils.isAllBlank(value);
        if (!isValid) {

            context.buildConstraintViolationWithTemplate(value).addConstraintViolation();
        }
        return !StringUtils.isNumeric(value) && !StringUtils.isAllBlank(value);
        }

    }

