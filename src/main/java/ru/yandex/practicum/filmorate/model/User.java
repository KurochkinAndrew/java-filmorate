package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.LoginConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
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
