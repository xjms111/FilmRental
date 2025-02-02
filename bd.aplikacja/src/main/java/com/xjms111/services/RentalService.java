package com.xjms111.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xjms111.entities.Movie;
import com.xjms111.entities.Rental;
import com.xjms111.entities.User;
import com.xjms111.repositories.RentalRepository;
import com.xjms111.repositories.MovieRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public RentalService(RentalRepository rentalRepository, MovieRepository movieRepository) {
        this.rentalRepository = rentalRepository;
        this.movieRepository = movieRepository;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Optional<Rental> getRentalById(int id) {
        return rentalRepository.findById(id);
    }

    public List<Rental> getRentalsByUserId(int userId) {
        return rentalRepository.findByUserId(userId);
    }

    public List<Rental> getRentalsByStatus(Rental.Status status) {
        return rentalRepository.findByStatus(status);
    }

    public Rental saveRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public void deleteRental(int id) {
        rentalRepository.deleteById(id);
    }
    public void rentMovie(User user, Movie movie) {
    if (movie.isAvailable()) {
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setMovie(movie);
        rental.setRentalDate(LocalDateTime.now());
        
        movie.setAvailability(false);
        
        rentalRepository.save(rental);
        movieRepository.save(movie);
    } else {
        throw new IllegalStateException("Movie is not available for rent.");
    }
}

}