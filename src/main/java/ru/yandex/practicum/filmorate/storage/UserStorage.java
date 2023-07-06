package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User createUser(User user);

    User refreshUser(User user);

    User getUserById(int id);

    HashSet<Integer> getAllId();
}
