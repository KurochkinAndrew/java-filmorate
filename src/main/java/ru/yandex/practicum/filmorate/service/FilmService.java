package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film refreshFilm(Film film) {
        return filmStorage.refreshFilm(film);
    }

    public Film likeTheFilm(int filmId, int userId) {
        userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        film.like(userId);
        return film;
    }

    public Film deleteLike(int filmId, int userId) {
        userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with an id = " + userId +
                    " didn't like a film with an id = " + filmId);
        }
        film.deleteLike(userId);
        return film;
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmStorage.getMostPopularFilms(count);
    }
}
