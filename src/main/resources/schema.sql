drop table if exists users cascade;
drop table if exists friends cascade;
drop table if exists mpa cascade;
drop table if exists genre cascade;
drop table if exists film cascade;
drop table if exists film_genre cascade;
drop table if exists likes cascade;

CREATE TABLE IF NOT EXISTS users(
user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email VARCHAR(320) NOT NULL UNIQUE,
login VARCHAR NOT NULL UNIQUE,
name VARCHAR,
birthday DATE
);

CREATE TABLE IF NOT EXISTS friends(
user_id INTEGER NOT NULL REFERENCES users(user_id),
friend_id INTEGER NOT NULL REFERENCES users(user_id),
status BOOLEAN NOT NULL,
PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS mpa(
mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS genre(
genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS film(
film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR NOT NULL ,
description VARCHAR(200) NOT NULL,
release_date DATE NOT NULL,
duration INTEGER NOT NULL CHECK (duration > 0),
mpa INTEGER NOT NULL REFERENCES mpa(mpa_id)
);

CREATE TABLE IF NOT EXISTS film_genre(
film_id INTEGER NOT NULL REFERENCES film(film_id),
genre_id INTEGER NOT NULL REFERENCES genre(genre_id),
PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes(
film_id INTEGER NOT NULL REFERENCES film(film_id),
user_id INTEGER NOT NULL REFERENCES users(user_id),
PRIMARY KEY (film_id, user_id)
);