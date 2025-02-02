package com.xjms111.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "Reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
@JoinColumn(name = "user_id", nullable = false)
private User user;

@ManyToOne
@JoinColumn(name = "movie_id", nullable = false)
private Movie movie;

@Column(name = "rating", nullable = false)
private int rating;

@Column(name = "comment")
private String comment;

public int getId() { return id; }
public void setId(int id) { this.id = id; }
public User getUser() { return user; }
public void setUser(User user) { this.user = user; }
public Movie getMovie() { return movie; }
public void setMovie(Movie movie) { this.movie = movie; }
public int getRating() { return rating; }
public void setRating(int rating) { this.rating = rating; }
public String getComment() { return comment; }
public void setComment(String comment) { this.comment = comment; }
}