package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.TreeSet;

@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public ArrayList<Film> getAll() {
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
        filmStorage.getFilmById(filmId).like(userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(int filmId, int userId) {
        userStorage.getUserById(userId);
        filmStorage.getFilmById(filmId);
        if (!filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with an id = " + userId +
                    " didn't like a film with an id = " + filmId);
        }
        filmStorage.getFilmById(filmId).deleteLike(userId);
        return filmStorage.getFilmById(filmId);
    }

    public ArrayList<Film> getMostPopularFilms(int count) {
        TreeSet<Film> treeSetOfFilms = new TreeSet<>(filmStorage.getAll());
        ArrayList<Film> mostPopularFilms = new ArrayList<>();
        Film film;
        for (int i = 0; i < count; i++) {
            film = treeSetOfFilms.pollLast();
            if (film == null) break;
            mostPopularFilms.add(film);
        }
        return mostPopularFilms;
    }
}
