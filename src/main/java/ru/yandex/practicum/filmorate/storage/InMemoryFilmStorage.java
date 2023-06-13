package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> films = new HashMap<>();
    private int newId = 1;

    public ArrayList<Film> getAll() {
        return new ArrayList<Film>(films.values());
    }

    public HashSet<Integer> getAllId() {
        return new HashSet<>(films.keySet());
    }

    public Film createFilm(Film film) {
        film.setId(newId);
        newId++;
        films.put(film.getId(), film);
        return film;
    }

    public Film refreshFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A film with an id = " + film.getId() +
                    " doesn't exist");
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A film with an id = " + id +
                    " doesn't exist");
        }
        return films.get(id);
    }
}
