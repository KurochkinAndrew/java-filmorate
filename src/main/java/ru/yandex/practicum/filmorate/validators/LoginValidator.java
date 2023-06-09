package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<LoginConstraint, String> {
    @Override
    public void initialize(LoginConstraint login) {
    }

    @Override
    public boolean isValid(String loginField, ConstraintValidatorContext cxt) {
        return loginField != null && !loginField.contains(" ") && !loginField.isBlank();
    }
}
