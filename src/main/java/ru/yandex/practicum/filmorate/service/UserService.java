package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

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
        friendsStorage.addFriend(id, friendId);
        return userStorage.getUserById(id);
    }

    public User deleteFriend(int id, int friendId) {
        friendsStorage.deleteFriend(id, friendId);
        return userStorage.getUserById(id);
    }

    public List<User> getFriendsOfUser(int id) {
        return friendsStorage.getFriendsOfUser(id);
    }

    public Set<Integer> getFriendsIdOfUser(int id) {
        return friendsStorage.getFriendsIdOfUser(id);
    }

    public List<User> getCommonFriends(int id1, int id2) {
        ArrayList<User> commonFriends = new ArrayList<>();
        Set<Integer> commonFriendsId = friendsStorage.getFriendsIdOfUser(id1);
        commonFriendsId.retainAll(friendsStorage.getFriendsIdOfUser(id2));
        for (int id : commonFriendsId) {
            commonFriends.add(userStorage.getUserById(id));
        }
        return commonFriends;
    }
}
