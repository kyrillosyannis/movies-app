drop table if exists users;
create table users (
                       id bigint auto_increment primary key,
                       username varchar(300),
                       password varchar(300)
);

drop table if exists movies;
create table movies (
    id bigint auto_increment primary key,
    title varchar(100),
    description varchar(300),
    user_id bigint references users(id),
    publication_date timestamp,
    likes int,
    hates int
);

drop table if exists users_movies;
create table users_movies (
    user_id bigint,
    movie_id bigint,
    like_hate_flag varchar(4),
    constraint users_movies_id unique (user_id, movie_id)
);
