package dev.katsiaryna.budgettracker.Outcome;

import dev.katsiaryna.budgettracker.Category.Category;
import dev.katsiaryna.budgettracker.Category.CategoryRepository;
import dev.katsiaryna.budgettracker.User.User;
import dev.katsiaryna.budgettracker.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for handling business logic related to Outcome (expenses).
 */
@Service
public class OutcomeService {

    private final OutcomeRepository outcomeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Constructor for dependency injection.
     */
    public OutcomeService(
            OutcomeRepository outcomeRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository
    ) {
        this.outcomeRepository = outcomeRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Adds a new outcome (expense) for a user in a specific category.
     * The amount will always be stored as a negative value.
     *
     * @param userId      the user ID
     * @param categoryId  the category ID
     * @param amount      the amount (positive, will be saved as negative)
     * @param description the description of the expense
     * @param date        the date of the transaction
     * @return the saved Outcome entity
     */
    @Transactional
    public Outcome addOutcome(Long userId, Long categoryId, Double amount, String description, LocalDate date) {
        if (userId == null || categoryId == null) {
            throw new RuntimeException("User ID and Category ID cannot be null");
        }

        // Fetch user from DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch category from DB
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Automatically convert to negative
        amount = Math.abs(amount) * -1;

        // Create and save the outcome entry
        Outcome outcome = new Outcome(user, category, amount, description, date);
        return outcomeRepository.save(outcome);
    }

    /**
     * Updates an existing outcome record.
     *
     * @param outcomeId   ID of the outcome to update
     * @param categoryId  new category ID
     * @param amount      new amount (positive, will be saved as negative)
     * @param description new description
     * @param date        new date
     * @return updated Outcome
     */
    @Transactional
    public Outcome updateOutcome(Long outcomeId, Long categoryId, Double amount, String description, LocalDate date) {
        Outcome outcome = outcomeRepository.findById(outcomeId)
                .orElseThrow(() -> new RuntimeException("Outcome not found"));

        // Fetch the new category from the DB
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Update fields
        outcome.setCategory(category);
        outcome.setAmount(Math.abs(amount) * -1); // Always negative
        outcome.setDescription(description);
        outcome.setDate(date);

        return outcomeRepository.save(outcome);
    }

    /**
     * Fetches all outcomes in the system.
     *
     * @return list of all outcomes
     */
    public List<Outcome> getAllOutcomes() {
        return outcomeRepository.findAll();
    }

    /**
     * Finds a single outcome by ID.
     *
     * @param id the ID of the outcome
     * @return optional outcome
     */
    public Optional<Outcome> getOutcomeById(Long id) {
        return outcomeRepository.findById(id);
    }

    /**
     * Fetches all outcomes for a specific user.
     *
     * @param userId the user ID
     * @return list of outcomes
     */
    public List<Outcome> getOutcomesByUser(Long userId) {
        return outcomeRepository.findByUserId(userId);
    }

    /**
     * Deletes an outcome by its ID.
     *
     * @param id the outcome ID
     */
    public void deleteOutcome(Long id) {
        if (!outcomeRepository.existsById(id)) {
            throw new RuntimeException("Income not found");
        }
        outcomeRepository.deleteById(id);
    }

    /**
     * Returns the total amount spent by a user in a specific category.
     *
     * @param userId     the user ID
     * @param categoryId the category ID
     * @return the total spent amount
     */
    public double getTotalSpentByUserAndCategory(Long userId, Long categoryId) {
        return outcomeRepository.sumByUserIdAndCategoryId(userId, categoryId);
    }

    /**
     * Returns the top N categories by total spending for a user.
     *
     * @param userId the user ID
     * @param topN   the number of top categories to return
     * @return map of category name to total spending (in descending order)
     */
    public Map<String, Double> getTopSpendingCategories(Long userId, int topN) {
        List<Object[]> rawResults = outcomeRepository.findTopCategoriesByAmount(userId);

        return rawResults.stream()
                .limit(topN)
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((Number) row[1]).doubleValue(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

}
