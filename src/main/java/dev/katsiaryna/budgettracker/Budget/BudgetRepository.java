package dev.katsiaryna.budgettracker.Budget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for accessing and managing {@link Budget} entities.
 * Extends {@link JpaRepository} to provide basic CRUD operations.
 */
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    /**
     * Retrieves all budget records for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of budgets associated with the given user
     */
    List<Budget> findByUserId(Long userId);

    /**
     * Retrieves budget entries for a specific user and category combination.
     *
     * @param userId     the ID of the user
     * @param categoryId the ID of the category
     * @return a list of budgets matching the user and category
     */
    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId AND b.category.category_id = :categoryId")
    List<Budget> findByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
}
