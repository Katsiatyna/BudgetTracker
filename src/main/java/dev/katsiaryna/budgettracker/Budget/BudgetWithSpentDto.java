package dev.katsiaryna.budgettracker.Budget;

/**
 * Data Transfer Object (DTO) representing a budget category
 * with its limit and the amount already spent.
 */
public class BudgetWithSpentDto {

    /** Name of the category (e.g., Food, Travel, etc.) */
    private String category;

    /** Budget limit set for this category */
    private double limit;

    /** Amount already spent in this category */
    private double spent;

    /**
     * Constructor to initialize all fields.
     *
     * @param category the name of the category
     * @param limit the budget limit for this category
     * @param spent the amount spent so far
     */
    public BudgetWithSpentDto(String category, double limit, double spent) {
        this.category = category;
        this.limit = limit;
        this.spent = spent;
    }

    // Getters
    /**
     * Gets the category name.
     *
     * @return category name
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the budget limit for the category.
     *
     * @return budget limit
     */
    public double getLimit() {
        return limit;
    }

    /**
     * Gets the amount spent in the category.
     *
     * @return amount spent
     */
    public double getSpent() {
        return spent;
    }
}
