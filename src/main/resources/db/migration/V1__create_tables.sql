create table if not exists users (
                       id serial primary key,
                       username varchar(300),
                       password varchar(300)
);

create table if not exists movies (
    id serial primary key,
    title varchar(100),
    description varchar(300),
    user_id bigint references users(id),
    username varchar(300),
    publication_date timestamp,
    likes int,
    hates int
);

create table if not exists users_movies (
    user_id bigint,
    movie_id bigint,
    like_hate_flag varchar(4),
    constraint users_movies_id unique (user_id, movie_id)
);
