package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpa", film.getMpa().getId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(newId.intValue());
        genreStorage.addToStorage(film);
        return film;
    }

    public Film refreshFilm(Film film) {
        getFilmById(film.getId());
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, " +
                "mpa = ? WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        genreStorage.updateGenre(film);
        return getFilmById(film.getId());
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
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa, count(l.USER_ID) " +
                "FROM film AS f LEFT JOIN likes AS l " +
                "ON f.FILM_ID = l.FILM_ID GROUP BY f.FILM_ID " +
                "ORDER BY count(l.USER_ID) DESC LIMIT ?";
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
}
