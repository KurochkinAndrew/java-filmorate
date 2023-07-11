package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<Integer> getLikes(int filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        List<Integer> likes = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"), filmId);
        if (likes == null) {
            return new ArrayList<>();
        }
        return likes;
    }

    public Like addLike(int filmId, int userId) {
        String sql = "INSERT INTO likes VALUES(?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        return new Like(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public int countLikes(int filmId) {
        return getLikes(filmId).size();
    }

}
