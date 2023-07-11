package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.HashSet;
import java.util.List;

@Component
@AllArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private LikesStorage likesStorage;
    private MpaStorage mpaStorage;
    private GenreStorage genreStorage;

    public List<Film> getAll() {
        String sql = "SELECT * FROM film";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(likesStorage.getLikes(rs.getInt("film_id"))),
                mpaStorage.getMpaById(rs.getInt("mpa")),
                new HashSet<>(genreStorage.getGenresOfFilm(rs.getInt("film_id"))),
                likesStorage.countLikes(rs.getInt("film_id"))));
    }


    public Film createFilm(Film film) {
        String sql = "INSERT INTO film(name, description, release_date, duration, mpa)" +
                "VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId());
        Film savedFilm = getFilmByName(film.getName());
        savedFilm.setGenres(film.getGenres());
        genreStorage.addToStorage(savedFilm);
        return savedFilm;
    }

    public Film refreshFilm(Film film) {
        getFilmById(film.getId());
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, " +
                "mpa = ? WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        Film refreshedFilm = getFilmByName(film.getName());
        refreshedFilm.setGenres(film.getGenres());
        genreStorage.updateGenre(refreshedFilm);
        return getFilmByName(film.getName());
    }

    public Film getFilmById(int id) {
        String sql = "SELECT * FROM film WHERE film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Film(rs.getInt("film_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getInt("duration"),
                    new HashSet<>(likesStorage.getLikes(rs.getInt("film_id"))),
                    mpaStorage.getMpaById(rs.getInt("mpa")),
                    new HashSet<>(genreStorage.getGenresOfFilm(rs.getInt("film_id"))),
                    likesStorage.countLikes(rs.getInt("film_id"))), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A film with an id = " + id +
                    " doesn't exist");
        }
    }

    public List<Film> getMostPopularFilms(int count) {
        String sql = "SELECT * FROM film WHERE film_id IN (SELECT film.film_id " +
                "FROM film " +
                "LEFT JOIN likes ON film.FILM_ID = likes.FILM_ID " +
                "GROUP BY FILM.FILM_ID " +
                "ORDER BY count(user_id) DESC) LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(likesStorage.getLikes(rs.getInt("film_id"))),
                mpaStorage.getMpaById(rs.getInt("mpa")),
                new HashSet<>(genreStorage.getGenresOfFilm(rs.getInt("film_id"))),
                likesStorage.countLikes(rs.getInt("film_id"))), count);
    }

    private Film getFilmByName(String name) {
        String sql = "SELECT * FROM film WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(likesStorage.getLikes(rs.getInt("film_id"))),
                mpaStorage.getMpaById(rs.getInt("mpa")),
                new HashSet<>(genreStorage.getGenresOfFilm(rs.getInt("film_id"))),
                likesStorage.countLikes(rs.getInt("film_id"))), name);
    }

}
