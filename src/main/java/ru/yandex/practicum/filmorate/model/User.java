package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.LoginConstraint;

import java.time.LocalDate;

@Builder
@Data
public class User {
    private int id;
    @Email
    private String email;
    @LoginConstraint
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
