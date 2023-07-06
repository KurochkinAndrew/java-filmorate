package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User refreshUser(User user) {
        return userStorage.refreshUser(user);
    }

    public User addFriend(int id, int friendId) {
        User user = userStorage.getUserById(id);
        user.addFriend(userStorage.getUserById(friendId).getId());
        userStorage.getUserById(friendId).addFriend(userStorage.getUserById(id).getId());
        return user;
    }

    public User deleteFriend(int id, int friendId) {
        User user = userStorage.getUserById(id);
        user.deleteFriend(userStorage.getUserById(friendId).getId());
        userStorage.getUserById(friendId).deleteFriend(userStorage.getUserById(id).getId());
        return user;
    }

    public List<User> getFriendsOfUser(int id) {
        ArrayList<User> friends = new ArrayList<>();
        for (int friendId : userStorage.getUserById(id).getFriends()) {
            if (userStorage.getUserById(friendId) != null) friends.add(userStorage.getUserById(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(int id1, int id2) {
        HashSet<Integer> userFriendsId = userStorage.getUserById(id1).getFriends();
        HashSet<Integer> otherUserFriendsId = userStorage.getUserById(id2).getFriends();
        ArrayList<User> commonFriends = new ArrayList<>();
        for (int id : userFriendsId) {
            if (otherUserFriendsId.contains(id)) {
                commonFriends.add(userStorage.getUserById(id));
            }
        }
        return commonFriends;
    }
}
