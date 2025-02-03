package com.example.secureblog.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    private static final int MIN_LENGTH = 14;
    private static final String DIGIT_PATTERN = ".*\\d.*";
    private static final String LOWERCASE_PATTERN = ".*[a-z].*";
    private static final String UPPERCASE_PATTERN = ".*[A-Z].*";
    private static final String SPECIAL_CHAR_PATTERN = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*";

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        if (password.length() < MIN_LENGTH) {
            context.buildConstraintViolationWithTemplate("Password must be at least " + MIN_LENGTH + " characters long")
                  .addConstraintViolation();
            isValid = false;
        }
        if (!password.matches(DIGIT_PATTERN)) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one digit")
                  .addConstraintViolation();
            isValid = false;
        }
        if (!password.matches(LOWERCASE_PATTERN)) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one lowercase letter")
                  .addConstraintViolation();
            isValid = false;
        }
        if (!password.matches(UPPERCASE_PATTERN)) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one uppercase letter")
                  .addConstraintViolation();
            isValid = false;
        }
        if (!password.matches(SPECIAL_CHAR_PATTERN)) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one special character")
                  .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
