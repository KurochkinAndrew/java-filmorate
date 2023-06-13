package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashSet;

public interface FilmStorage {
    ArrayList<Film> getAll();

    HashSet<Integer> getAllId();

    Film createFilm(Film film);

    Film refreshFilm(Film film);

    Film getFilmById(int id);
}
