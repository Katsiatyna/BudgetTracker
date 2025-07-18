package dev.katsiaryna.budgettracker.User;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents an application user entity.
 * Implements Spring Security's {@link UserDetails} to integrate with authentication mechanisms.
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username for login. Must be unique and non-null.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Hashed password for authentication.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Email address of the user. Optional but must be unique if present.
     */
    @Column(unique = true)
    private String email;

    /**
     * Constructs a user with username and password.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Constructs a user with ID, username and password.
     */
    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructs a user with just an ID.
     */
    public User(Long id) {
        this.id = id;
    }

    //Default constructor
    public User() {
    }

    //Getters
    /**
     * Gets the password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Gets the user's ID.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Gets the username for login.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the email address.
     */
    public String getEmail() {
        return email;
    }

    //Setters
    /**
     * Sets the user's ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    // --- Spring Security overrides ---

    /**
     * Returns the authorities granted to the user.
     * Currently returns an empty list (no roles implemented).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); //No roles for now
    }

    /**
     * Indicates whether the user's account has expired.
     * Always true (expiration not used).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; //Won't use exp logic
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * Always true (lock feature not used).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; //Won't use locked logic
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     * Always true (password expiration not used).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; //Won't use password expiration logic
    }

    /**
     * Indicates whether the user is enabled.
     * Always true (activation not used).
     */
    @Override
    public boolean isEnabled() {
        return true; //Won't use activation logic
    }

    /**
     * Returns a string representation of the user object.
     */
    public String toString() {
        return "User(id=" + this.getId() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ")";
    }
}
