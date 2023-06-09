package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateConstraint, LocalDate> {
    @Override
    public void initialize(ReleaseDateConstraint localDate) {
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext cxt) {
        return localDate != null && localDate.isAfter(LocalDate.of(1895, 12, 27));
    }
}
