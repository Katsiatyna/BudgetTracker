package dev.katsiaryna.budgettracker.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing Category data from the database.
 * Extends JpaRepository to provide CRUD and query methods.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds a category by its unique name.
     *
     * @param category the name of the category
     * @return an Optional containing the category if found, otherwise empty
     */
    Optional<Category> findByCategory(String category);
}
