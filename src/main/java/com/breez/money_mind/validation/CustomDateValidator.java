package com.breez.money_mind.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomDateValidator implements ConstraintValidator<CustomDateValid, String> {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		try {
			LocalDate.parse(value, FORMATTER);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}

}
