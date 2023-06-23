package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    HashSet<Integer> getAllId();

    Film createFilm(Film film);

    Film refreshFilm(Film film);

    Film getFilmById(int id);

    List<Film> getMostPopularFilms(int count);
}
