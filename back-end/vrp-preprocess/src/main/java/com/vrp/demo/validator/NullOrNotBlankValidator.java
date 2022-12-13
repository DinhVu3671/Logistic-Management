package com.vrp.demo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

	@Override
	public void initialize(final NullOrNotBlank constraintAnnotation) {
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		return value == null || !value.trim().isEmpty();
	}
}