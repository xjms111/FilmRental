package com.xjms111.repositories;

import com.xjms111.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    
    // Custom query to find movies by genre
    List<Movie> findByGenre(String genre);
    
    // Custom query to find available movies
    List<Movie> findByAvailabilityTrue();
}