package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<Genre> getGenresOfFilm(int id) {
        String sql = "SELECT f.genre_id, name FROM film_genre AS f INNER JOIN genre AS g ON f.genre_id = g.genre_id  " +
                "WHERE film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                rs.getString("name")), id);
        if (genres == null) {
            return new ArrayList<>();
        }
        return genres;
    }

    public void addToStorage(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
                        film.getId(), genre.getId());
            }
        }
    }

    public void updateGenre(Film film) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", film.getId());
        addToStorage(film);
    }

    public List<Genre> getAll() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                        rs.getString("name")));
    }

    public Genre getGenreById(int id) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                    rs.getString("name")), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre with an id = " + id + " not exists");
        }
    }
}
