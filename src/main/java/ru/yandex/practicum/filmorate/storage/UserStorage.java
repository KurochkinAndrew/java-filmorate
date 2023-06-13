package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;

public interface UserStorage {
    ArrayList<User> getAll();

    User createUser(User user);

    User refreshUser(User user);

    User getUserById(int id);

    HashSet<Integer> getAllId();
}
