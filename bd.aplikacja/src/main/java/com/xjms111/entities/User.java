package com.xjms111.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Email
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    // Reference to Role entity
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;  // Corrected: refers to Role entity directly

    @OneToMany(mappedBy = "user")
    private List<Rental> rentals;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @Transient
    public final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PrePersist
    public void hashPassword() {
        this.password = passwordEncoder.encode(this.password);
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;  // Correct getter for Role
    }

    public void setRole(Role role) {
        this.role = role;  // Correct setter for Role
    }

    public List<Rental> getRentals() {
        return rentals;
    }
    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public List<Review> getReviews() {
        return reviews;
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
