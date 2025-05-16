package dev.katsiaryna.budgettracker.Budget;

import dev.katsiaryna.budgettracker.Category.Category;
import dev.katsiaryna.budgettracker.Outcome.OutcomeRepository;
import dev.katsiaryna.budgettracker.User.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for managing budget-related logic,
 * such as setting category limits, calculating total limits,
 * generating warnings, and fetching budget summaries.
 */
@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final OutcomeRepository outcomeRepository;

    /**
     * Constructor injection for dependencies.
     *
     * @param budgetRepository the budget repository
     * @param outcomeRepository the outcome repository
     */
    public BudgetService(BudgetRepository budgetRepository, OutcomeRepository outcomeRepository) {
        this.budgetRepository = budgetRepository;
        this.outcomeRepository = outcomeRepository;
    }

    /**
     * Fetches all budgets associated with a specific user.
     *
     * @param userId the user's ID
     * @return list of budgets
     */
    public List<Budget> getBudgetsByUser(Long userId) {
        return budgetRepository.findByUserId(userId);
    }

    /**
     * Calculates the total budget limit for a user by summing all their category limits.
     *
     * @param userId the user's ID
     * @return total budget limit
     */
    public double getTotalLimitForUser(Long userId) {
        return budgetRepository.findByUserId(userId).stream()
                .mapToDouble(Budget::getLimitAmount)
                .sum();
    }

    /**
     * Adds a new budget to the database.
     *
     * @param budget the budget to save
     * @return the saved budget
     */
    public Budget addBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    /**
     * Creates or updates a budget limit for a specific user and category.
     *
     * @param userId       the user's ID
     * @param categoryId   the category's ID
     * @param limitAmount  the limit to set
     */
    public void setCategoryLimit(Long userId, Long categoryId, Double limitAmount) {
        List<Budget> existing = budgetRepository.findByUserIdAndCategoryId(userId, categoryId);
        if (!existing.isEmpty()) {
            Budget budget = existing.get(0);
            budget.setLimitAmount(limitAmount);
            budgetRepository.save(budget);
        } else {
            Budget newBudget = new Budget();
            newBudget.setUser(new User(userId));
            newBudget.setCategory(new Category(categoryId));
            newBudget.setLimitAmount(limitAmount);
            budgetRepository.save(newBudget);
        }
    }

    /**
     * Deletes a budget entry by ID.
     *
     * @param id the budget ID
     */
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    /**
     * Updates the limit amount of a budget entry by ID.
     *
     * @param id        the budget ID
     * @param newLimit  the new limit to set
     */
    public void updateLimitAmount(Long id, Double newLimit) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        budget.setLimitAmount(newLimit);
        budgetRepository.save(budget);
    }

    /**
     * Returns a list of category warnings where spending exceeds 90% of the limit.
     *
     * @param userId the user's ID
     * @return list of warnings with category name, spent amount, and limit
     */
    public List<CategoryWarningDto> getCategoryWarnings(Long userId) {
        List<Budget> budgets = budgetRepository.findByUserId(userId);
        List<CategoryWarningDto> warnings = new ArrayList<>();

        for (Budget budget : budgets) {
            Category category = budget.getCategory();
            double limit = budget.getLimitAmount();
            double spent = outcomeRepository.sumByUserIdAndCategoryId(userId, category.getCategory_id());

            if (spent >= 0.9 * limit) {
                warnings.add(new CategoryWarningDto(category.getCategory(), spent, limit));
            }
        }

        return warnings;
    }

    /**
     * Retrieves all budget entries for a user with the corresponding spent amounts.
     *
     * @param userId the user's ID
     * @return list of budgets with actual spent values
     */
    public List<BudgetWithSpentDto> getBudgetsWithSpent(Long userId, String from, String to) {
        LocalDate start = LocalDate.parse(from);
        LocalDate end = LocalDate.parse(to);
        List<Budget> budgets = budgetRepository.findByUserId(userId);
        List<BudgetWithSpentDto> result = new ArrayList<>();

        for (Budget budget : budgets) {
            if (budget.getCategory() == null) continue;
            String categoryName = budget.getCategory().getCategory();
            double limit = budget.getLimitAmount();
            double spent = outcomeRepository.sumByUserAndCategoryBetweenDates(userId, budget.getCategory().getCategory_id(), start, end);


            result.add(new BudgetWithSpentDto(categoryName, limit, spent));
        }

        return result;
    }

}
