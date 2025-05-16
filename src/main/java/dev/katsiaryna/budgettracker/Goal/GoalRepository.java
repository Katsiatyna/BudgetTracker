package dev.katsiaryna.budgettracker.Goal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for accessing and managing Goal entities in the database.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface GoalRepository extends JpaRepository<Goal, Long> {

    /**
     * Retrieves all goals associated with a specific user.
     *
     * @param userId the ID of the user
     * @return list of goals belonging to the user
     */
    List<Goal> findByUser_Id(Long userId);
}
