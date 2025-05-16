package dev.katsiaryna.budgettracker.Outcome;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Repository interface for Outcome entity.
 * Provides custom queries in addition to CRUD operations.
 */
public interface OutcomeRepository extends JpaRepository<Outcome, Long> {

    /**
     * Finds all outcomes for a specific user.
     *
     * @param userId ID of the user.
     * @return List of Outcome objects.
     */
    List<Outcome> findByUserId(Long userId);

    /**
     * Sums the total spent amount for a given user and category.
     * The amount is returned as absolute value to ensure consistency.
     *
     * @param userId     ID of the user.
     * @param categoryId ID of the category.
     * @return Total absolute spending amount.
     */
    @Query("SELECT COALESCE(SUM(ABS(o.amount)), 0) FROM Outcome o WHERE o.user.id = :userId AND o.category.category_id = :categoryId")
    double sumByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);

    /**
     * Returns the top spending categories for a user,
     * ordered by the highest total absolute amount.
     *
     * @param userId ID of the user.
     * @return List of Object arrays containing category name and total spent amount.
     */
    @Query(value = """
    SELECT c.category, SUM(ABS(o.amount)) AS total 
    FROM Outcome o 
    JOIN o.category c 
    WHERE o.user.id = :userId 
    GROUP BY c.category 
    ORDER BY total DESC
    """)
    List<Object[]> findTopCategoriesByAmount(@Param("userId") Long userId);

    /**
     * Finds the top spending categories for a user within a specific date range.
     * @param userId
     * @param start
     * @param end
     * @return
     */
    @Query(value = """
    SELECT c.category_name AS category, SUM(o.amount) AS total 
    FROM outcomes o
    JOIN category c ON o.category_id = c.category_id
    WHERE o.user_id = :userId AND o.date BETWEEN :start AND :end
    GROUP BY c.category_name
    ORDER BY total DESC
    LIMIT 5
""", nativeQuery = true)
    List<Object[]> findTopSpendingCategoriesByUserAndDate(
            @Param("userId") Long userId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);


    /**
     * Finds the total amount spent on a specific category for a user within a specific date range.
     * @param userId
     * @param categoryId
     * @param start
     * @param end
     * @return
     */
    @Query(value = """
    SELECT COALESCE(SUM(ABS(o.amount)), 0)
    FROM outcomes o
    WHERE o.user_id = :userId AND o.category_id = :categoryId
      AND o.date BETWEEN :start AND :end
""", nativeQuery = true)
    double sumByUserAndCategoryBetweenDates(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    /**
     *
     * @param userId
     * @param start
     * @param end
     * @return
     */
    @Query(value = """
    SELECT c.category_name AS category, SUM(o.amount) AS total
    FROM outcomes o
    JOIN category c ON o.category_id = c.category_id
    WHERE o.user_id = :userId AND o.date BETWEEN :start AND :end
    GROUP BY c.category_name
""", nativeQuery = true)
    List<Object[]> sumByCategoryNameBetweenDates(@Param("userId") Long userId,
                                                 @Param("start") LocalDate start,
                                                 @Param("end") LocalDate end);



}
