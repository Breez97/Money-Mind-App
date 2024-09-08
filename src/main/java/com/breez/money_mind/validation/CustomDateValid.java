package com.breez.money_mind.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = CustomDateValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomDateValid {

	String message() default "Invalid date format";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
