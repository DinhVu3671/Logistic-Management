package com.vrp.demo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public void initialize(final PhoneNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(final String phoneNumber, final ConstraintValidatorContext context) {
        return phoneNumber.matches("(09|03|07|08|05)+([0-9]{8})\\b");
    }
}
