package dev.katsiaryna.budgettracker.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing {@link User} entities from the database.
 * Extends {@link JpaRepository} to provide CRUD operations and more.
 */
@Repository // Marks this interface as a Spring Data repository for dependency injection
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the user if found, or empty if not
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their ID.
     *
     * @param id the ID to search for
     * @return an {@link Optional} containing the user if found, or empty if not
     */
    Optional<User> findById(Long id);

}
