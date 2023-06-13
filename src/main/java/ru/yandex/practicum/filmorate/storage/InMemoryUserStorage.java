package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();
    private int newId = 1;

    public ArrayList<User> getAll() {
        return new ArrayList<User>(users.values());
    }

    public HashSet<Integer> getAllId() {
        return new HashSet<>(users.keySet());
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(newId);
        newId++;
        users.put(user.getId(), user);
        return user;
    }

    public User refreshUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (!users.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with an id = " + user.getId() +
                    " doesn't exist");
        }
        users.put(user.getId(), user);
        return user;
    }

    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with an id = " + id +
                    " doesn't exist");
        }
        return users.get(id);
    }
}
