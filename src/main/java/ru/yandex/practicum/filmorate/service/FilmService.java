package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;

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
        likesStorage.addLike(filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(int filmId, int userId) {
        userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        if (likesStorage.getLikes(filmId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with an id = " + userId +
                    " didn't like a film with an id = " + filmId);
        }
        likesStorage.deleteLike(filmId, userId);
        return film;
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmStorage.getMostPopularFilms(count);
    }
}
