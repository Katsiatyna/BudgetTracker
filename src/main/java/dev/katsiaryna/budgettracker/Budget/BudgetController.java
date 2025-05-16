package dev.katsiaryna.budgettracker.Budget;

import dev.katsiaryna.budgettracker.User.UserRepository;
import dev.katsiaryna.budgettracker.User.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling budget-related operations such as
 * setting limits, updating them, retrieving warnings, and listing budgets.
 */
@RestController
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final UserRepository userRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param budgetService the service to manage budget operations
     * @param userRepository repository to fetch user data
     */
    public BudgetController(BudgetService budgetService, UserRepository userRepository) {
        this.budgetService = budgetService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all budget entries for a given user.
     *
     * @param userId the ID of the user
     * @return a list of budgets
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Budget>> getBudgetsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(budgetService.getBudgetsByUser(userId));
    }

    /**
     * Retrieves the total budget limit assigned to a user.
     *
     * @param userId the ID of the user
     * @return the total budget limit as a Double
     */
    @GetMapping("/total-limit/{userId}")
    public ResponseEntity<Double> getTotalLimit(@PathVariable Long userId) {
        return ResponseEntity.ok(budgetService.getTotalLimitForUser(userId));
    }

    /**
     * Sets or updates a category-specific budget limit for a user.
     *
     * @param request the budget request containing userId, categoryId, and limitAmount
     * @return success message
     */
    @PostMapping("/set-category-limit")
   public ResponseEntity<String> setCategoryLimit(@RequestBody BudgetRequest request) {
        budgetService.setCategoryLimit(request.getUserId(), request.getCategoryId(), request.getLimitAmount());
        return ResponseEntity.ok("Category limit set successfully.");
   }

    /**
     * Deletes a budget entry by its ID.
     *
     * @param id the ID of the budget to delete
     * @return success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.ok("Budget deleted successfully");
    }

    /**
     * Updates the limit amount for an existing budget.
     *
     * @param id the ID of the budget to update
     * @param request the request containing the new limit amount
     * @return success message
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategoryLimit(@PathVariable Long id, @RequestBody BudgetRequest request) {
        budgetService.updateLimitAmount(id, request.getLimitAmount());
        return ResponseEntity.ok("Budget updated successfully");
    }

    /**
     * Retrieves a list of warning messages for categories where spending is nearing the limit.
     *
     * @param userId the ID of the user
     * @return a list of category warnings
     */
    @GetMapping("/warnings/{userId}")
    public ResponseEntity<List<CategoryWarningDto>> getCategoryWarnings(@PathVariable Long userId) {
        List<CategoryWarningDto> warnings = budgetService.getCategoryWarnings(userId);
        return ResponseEntity.ok(warnings);
    }

    /**
     * Retrieves budgets with their associated spending amounts.
     * Used to show how much of the budget has been spent per category.
     *
     * @param userId the ID of the user
     * @return a list of budgets with spent values
     */
    @GetMapping("/user-with-spent/{userId}")
    public ResponseEntity<List<BudgetWithSpentDto>> getBudgetsWithSpent(
            @PathVariable Long userId,
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(budgetService.getBudgetsWithSpent(userId, from, to));
    }

}