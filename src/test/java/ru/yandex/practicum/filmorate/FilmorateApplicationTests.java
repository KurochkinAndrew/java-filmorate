package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {
    final FilmDbStorage filmDbStorage;
    final UserDbStorage userDbStorage;
    final LikesStorage likesStorage;
    final GenreStorage genreStorage;
    final MpaStorage mpaStorage;
    final FriendsStorage friendsStorage;
    Film film;
    Film film1;
    User user;
    User user1;
    User user2;

    @BeforeEach
    public void beforeEach() {
        film = new Film();
        film.setName("test name");
        film.setDescription("test description");
        film.setDuration(210);
        film.setReleaseDate(LocalDate.of(2022, 8, 10));
        film.setMpa(new Mpa(1, "G"));
        film1 = new Film();
        film1.setName("test name1");
        film1.setDescription("test description1");
        film1.setDuration(210);
        film1.setReleaseDate(LocalDate.of(2022, 8, 10));
        film1.setMpa(new Mpa(1, "G"));
        user = new User(1, "test@gmail.com", "test login", "test name", LocalDate.of(2000, 3, 1));
        user1 = new User(2, "test1@gmail.com", "test login1", "test name1", LocalDate.of(2000, 3, 1));
        user2 = new User(3, "test2@gmail.com", "test login2", "test name2", LocalDate.of(2000, 3, 1));
    }

    @Test
    public void testGetFilmById() {
        filmDbStorage.createFilm(film);
        Film received = filmDbStorage.getFilmById(1);
        assertEquals(film.getName(), received.getName());
        assertEquals(film.getDescription(), received.getDescription());
        assertEquals(film.getDuration(), received.getDuration());
        assertEquals(film.getReleaseDate(), received.getReleaseDate());
    }

    @Test
    public void testGetAllFilms() {
        filmDbStorage.createFilm(film);
        filmDbStorage.createFilm(film1);
        ArrayList<Film> films = new ArrayList<>(filmDbStorage.getAll());
        assertEquals(films.get(0).getName(), film.getName());
        assertEquals(films.get(0).getDescription(), film.getDescription());
        assertEquals(films.get(0).getDuration(), film.getDuration());
        assertEquals(films.get(0).getReleaseDate(), film.getReleaseDate());

        assertEquals(films.get(1).getName(), film1.getName());
        assertEquals(films.get(1).getDescription(), film1.getDescription());
        assertEquals(films.get(1).getDuration(), film1.getDuration());
        assertEquals(films.get(1).getReleaseDate(), film1.getReleaseDate());
    }

    @Test
    public void testRefreshFilm() {
        filmDbStorage.createFilm(film);
        film1.setId(1);
        filmDbStorage.refreshFilm(film1);
        Film received = filmDbStorage.getFilmById(1);
        assertEquals(film1.getName(), received.getName());
        assertEquals(film1.getDescription(), received.getDescription());
        assertEquals(film1.getDuration(), received.getDuration());
        assertEquals(film1.getReleaseDate(), received.getReleaseDate());
    }


    @Test
    public void testGetMostPopularFilms() {
        filmDbStorage.createFilm(film);
        filmDbStorage.createFilm(film1);
        userDbStorage.createUser(user);
        userDbStorage.createUser(user1);
        userDbStorage.createUser(user2);
        likesStorage.addLike(1, 1);
        ArrayList<Film> mostPopular = new ArrayList<>(filmDbStorage.getMostPopularFilms(2));
        assertEquals(film.getName(), mostPopular.get(0).getName());
        assertEquals(film.getDescription(), mostPopular.get(0).getDescription());
        assertEquals(film.getDuration(), mostPopular.get(0).getDuration());
        assertEquals(film.getReleaseDate(), mostPopular.get(0).getReleaseDate());
        likesStorage.addLike(2, 2);
        likesStorage.addLike(2, 3);
        mostPopular = new ArrayList<>(filmDbStorage.getMostPopularFilms(2));
        assertEquals(film1.getName(), mostPopular.get(0).getName());
        assertEquals(film1.getDescription(), mostPopular.get(0).getDescription());
        assertEquals(film1.getDuration(), mostPopular.get(0).getDuration());
        assertEquals(film1.getReleaseDate(), mostPopular.get(0).getReleaseDate());
    }

    @Test
    void testCreateFilm() {
        filmDbStorage.createFilm(film);
        assertEquals(1, filmDbStorage.getAll().size());
    }

    @Test
    public void testGetUserById() {
        userDbStorage.createUser(user);
        User received = userDbStorage.getUserById(1);
        assertEquals(user.getName(), received.getName());
        assertEquals(user.getEmail(), received.getEmail());
        assertEquals(user.getLogin(), received.getLogin());
        assertEquals(user.getBirthday(), received.getBirthday());
    }

    @Test
    public void testGetAllUsers() {
        userDbStorage.createUser(user);
        userDbStorage.createUser(user1);
        ArrayList<User> users = new ArrayList<>(userDbStorage.getAll());
        assertEquals(users.get(0).getName(), user.getName());
        assertEquals(users.get(0).getEmail(), user.getEmail());
        assertEquals(users.get(0).getLogin(), user.getLogin());
        assertEquals(users.get(0).getBirthday(), user.getBirthday());

        assertEquals(users.get(1).getName(), user1.getName());
        assertEquals(users.get(1).getEmail(), user1.getEmail());
        assertEquals(users.get(1).getLogin(), user1.getLogin());
        assertEquals(users.get(1).getBirthday(), user1.getBirthday());
    }

    @Test
    public void testRefreshUser() {
        userDbStorage.createUser(user);
        user1.setId(1);
        userDbStorage.refreshUser(user1);
        User received = userDbStorage.getUserById(1);
        assertEquals(user1.getName(), received.getName());
        assertEquals(user1.getEmail(), received.getEmail());
        assertEquals(user1.getLogin(), received.getLogin());
        assertEquals(user1.getBirthday(), received.getBirthday());
    }

    @Test
    void testCreateUser() {
        userDbStorage.createUser(user);
        assertEquals(1, userDbStorage.getAll().size());
    }

    @Test
    void testAddLike() {
        filmDbStorage.createFilm(film);
        userDbStorage.createUser(user);
        assertEquals(0, likesStorage.getLikes(1).size());
        likesStorage.addLike(1, 1);
        assertEquals(1, likesStorage.getLikes(1).size());
    }

    @Test
    void testDeleteLike() {
        filmDbStorage.createFilm(film);
        userDbStorage.createUser(user);
        likesStorage.addLike(1, 1);
        assertEquals(1, likesStorage.getLikes(1).size());
        likesStorage.deleteLike(1, 1);
        assertEquals(0, likesStorage.getLikes(1).size());
    }

    @Test
    void testFriendStorage() {
        userDbStorage.createUser(user);
        userDbStorage.createUser(user1);
        friendsStorage.addFriend(1, 2);
        assertEquals(1, friendsStorage.getFriendsIdOfUser(1).size());
        assertTrue(friendsStorage.getFriendsIdOfUser(1).contains(2));
        assertEquals(user1.getEmail(), friendsStorage.getFriendsOfUser(1).get(0).getEmail());
        friendsStorage.deleteFriend(1, 2);
        assertEquals(0, friendsStorage.getFriendsIdOfUser(1).size());
    }
}