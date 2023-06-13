package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.LoginConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;

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
    private final HashSet<Integer> friends = new HashSet<>();

    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }
}
