package dev.katsiaryna.budgettracker.Income;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Repository interface for managing {@link Income} entities.
 * Extends JpaRepository to provide standard CRUD operations.
 */
public interface IncomeRepository extends JpaRepository<Income, Long> {

    /**
     * Finds all income records associated with a specific user.
     *
     * @param userId the ID of the user
     * @return list of {@link Income} entities for the user
     */
    List<Income> findByUserId(Long userId);

    /**
     * Allows categories to be shown on the charts according to the dates
     * @param userId
     * @param start
     * @param end
     * @return
     */
    @Query(value = """
    SELECT c.category AS category, SUM(i.amount) AS total
    FROM incomes i
    JOIN category c ON i.category_id = c.category_id
    WHERE i.user_id = :userId AND i.date BETWEEN :start AND :end
    GROUP BY c.category
""", nativeQuery = true)
    List<Object[]> sumByCategoryNameBetweenDates(@Param("userId") Long userId,
                                                 @Param("start") LocalDate start,
                                                 @Param("end") LocalDate end);

}
