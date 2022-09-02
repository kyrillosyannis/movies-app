package com.studio.movierama.repository;

import com.studio.movierama.domain.UserMovie;
import com.studio.movierama.domain.UserMovieId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovie, UserMovieId> {
}
