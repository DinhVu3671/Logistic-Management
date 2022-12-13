package com.vrp.demo.validator;



import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String message;

    public void initialize(final FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
        this.message = constraintAnnotation.message();
    }

    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            final Object secondObj = BeanUtils.getProperty(value, secondFieldName);

            valid = firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        } catch (final Exception ignore) {
            // ignore
        }

        if (!valid) {
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(firstFieldName)
                    .addConstraintViolation().disableDefaultConstraintViolation();
        }

        return valid;
    }
}