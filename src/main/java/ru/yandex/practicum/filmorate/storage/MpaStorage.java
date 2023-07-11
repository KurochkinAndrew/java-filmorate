package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public Mpa getMpaById(int id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Mpa(rs.getInt("mpa_id"),
                    rs.getString("name")), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MPA with an id = " + id + " not exists");
        }
    }

    public List<Mpa> getAll() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Mpa(rs.getInt("mpa_id"),
                        rs.getString("name")));
    }
}
