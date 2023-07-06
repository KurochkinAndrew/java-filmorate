package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> films = new HashMap<>();
    private TreeSet<Film> treeSetOfFilms = new TreeSet<>();
    private int newId = 1;

    public List<Film> getAll() {
        return new ArrayList<Film>(films.values());
    }

    public HashSet<Integer> getAllId() {
        return new HashSet<>(films.keySet());
    }

    public Film createFilm(Film film) {
        film.setId(newId);
        newId++;
        films.put(film.getId(), film);
        treeSetOfFilms.add(film);
        return film;
    }

    public Film refreshFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A film with an id = " + film.getId() +
                    " doesn't exist");
        }
        System.out.println("статус " + treeSetOfFilms.remove(films.get(film.getId())));
        films.put(film.getId(), film);
        treeSetOfFilms.add(film);
        return film;
    }

    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A film with an id = " + id +
                    " doesn't exist");
        }
        return films.get(id);
    }

    public List<Film> getMostPopularFilms(int count) {
        TreeSet<Film> setOfFilms = new TreeSet<>(treeSetOfFilms);
        ArrayList<Film> mostPopularFilms = new ArrayList<>();
        Film film;
        for (int i = 0; i < count; i++) {
            film = setOfFilms.pollLast();
            if (film == null) break;
            mostPopularFilms.add(film);
        }
        return mostPopularFilms;
    }
}
