package dev.katsiaryna.budgettracker.Income;

import dev.katsiaryna.budgettracker.Category.Category;
import dev.katsiaryna.budgettracker.Category.CategoryRepository;
import dev.katsiaryna.budgettracker.User.User;
import dev.katsiaryna.budgettracker.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling business logic related to income operations.
 */
@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Constructor for IncomeService.
     *
     * @param incomeRepository   The income repository for database operations.
     * @param userRepository     The user repository to fetch user data.
     * @param categoryRepository The category repository to fetch category data.
     */
    public IncomeService(IncomeRepository incomeRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Adds a new income entry for the specified user and category.
     *
     * @param userId      The ID of the user.
     * @param categoryId  The ID of the category.
     * @param amount      The amount of income (must be positive).
     * @param description The description of the income.
     * @param date        The date the income occurred.
     * @return The saved Income entity.
     */
    @Transactional
    public Income addIncome(Long userId, Long categoryId, Double amount, String description, LocalDate date) {
        if (userId == null || categoryId == null) {
            throw new RuntimeException("User ID and Category ID cannot be null");
        }

        // Fetch user from DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch category from DB
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (amount <= 0) {
            throw new RuntimeException("Amount must be positive");
        }

        // Create and save the income entry
        Income income = new Income(user, category, amount, description, date);
        return incomeRepository.save(income);
    }

    /**
     * Updates an existing income entry with new values.
     *
     * @param incomeId    The ID of the income to update.
     * @param categoryId  The new category ID.
     * @param amount      The new amount.
     * @param description The new description.
     * @param date        The new date.
     * @return The updated Income entity.
     */
    @Transactional
    public Income updateIncome(Long incomeId, Long categoryId, Double amount, String description, LocalDate date) {
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        // Fetch the new category from the DB
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Update fields
        income.setCategory(category);
        income.setAmount(amount);
        income.setDescription(description);
        income.setDate(date);

        return incomeRepository.save(income);
    }

    /**
     * Retrieves all income entries from the database.
     *
     * @return A list of all income entries.
     */
    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    /**
     * Finds an income entry by its ID.
     *
     * @param id The ID of the income.
     * @return An Optional containing the income if found, otherwise empty.
     */
    public Optional<Income> getIncomeById(Long id) {
        return incomeRepository.findById(id);
    }

    /**
     * Retrieves all income entries for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of income entries belonging to the user.
     */
    public List<Income> getIncomesByUser(Long userId) {
        return incomeRepository.findByUserId(userId);
    }

    /**
     * Deletes an income entry by its ID.
     *
     * @param id The ID of the income to delete.
     */
    public void deleteIncome(Long id) {
        if (!incomeRepository.existsById(id)) {
            throw new RuntimeException("Income not found");
        }
        incomeRepository.deleteById(id);
    }
}
