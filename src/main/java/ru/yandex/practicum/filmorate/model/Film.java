package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.ReleaseDateConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;

@Data
@Builder
public class Film implements Comparable<Film> {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @ReleaseDateConstraint
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private final HashSet<Integer> likes = new HashSet<>();

    public void like(int id) {
        likes.add(id);
    }

    public void deleteLike(int id) {
        likes.remove(id);
    }

    public int compareTo(Film o) {
        if (likes.size() - o.getLikes().size() == 0) {
            return 1;
        }
        return likes.size() - o.getLikes().size();
    }
}
