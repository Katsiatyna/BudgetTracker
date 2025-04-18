package dev.katsiaryna.budgettracker.User;

import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //The bridge between the application and the database
public interface UserRepositorty extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}
