package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FriendsStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    public void addFriend(int userId, int friendId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);
        String sql = "INSERT INTO friends VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, false);
    }

    public Set<Integer> getFriendsIdOfUser(int userId) {
        userStorage.getUserById(userId);
        String sql = "SELECT friend_id FROM friends WHERE user_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("friend_id"), userId));
    }

    public void deleteFriend(int userId, int friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<User> getFriendsOfUser(int userId) {
        userStorage.getUserById(userId);
        String sql = "SELECT * FROM users WHERE user_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(rs.getInt("user_id"),
                rs.getString("email"), rs.getString("login"), rs.getString("name"),
                rs.getDate("birthday").toLocalDate()), userId);
    }
}
