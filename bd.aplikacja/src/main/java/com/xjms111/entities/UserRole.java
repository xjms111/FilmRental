package com.xjms111.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "UserRoles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    public UserRole() {}

    public UserRole(User user, RoleEntity role) {
        this.user = user;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public RoleEntity getRole() { return role; }
    public void setRole(RoleEntity role) { this.role = role; }
}