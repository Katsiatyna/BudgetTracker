package dev.katsiaryna.budgettracker.Budget;

/**
 * Data Transfer Object (DTO) for handling budget-related requests.
 * This class carries data for setting or updating budget limits
 * for a specific user and category.
 */
public class BudgetRequest {

    /**
     * The ID of the user who owns the budget.
     */
    private Long userId;

    /**
     * The ID of the category the budget is associated with.
     */
    private Long categoryId;

    /**
     * The limit amount for the budget.
     */
    private Double limitAmount;

    /**
     * Default constructor for deserialization.
     */
    public BudgetRequest() {}

    /**
     * Constructor with userId and limitAmount.
     *
     * @param userId the ID of the user
     * @param limitAmount the amount to set as the budget limit
     */
    public BudgetRequest(Long userId, Double limitAmount) {
        this.userId = userId;
        this.limitAmount = limitAmount;
    }

    // Getters

    /**
     * Gets the user ID.
     *
     * @return userId
     */
    public Long getUserId() {
        return userId;
        }

    /**
     * Gets the category ID.
     *
     * @return categoryId
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Gets the limit amount.
     *
     * @return limitAmount
     */
    public Double getLimitAmount() {
        return limitAmount;
        }

    // Setters
    /**
     * Sets the user ID.
     *
     * @param userId the ID of the user
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Sets the category ID.
     *
     * @param categoryId the ID of the category
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Sets the limit amount.
     *
     * @param limitAmount the budget limit to set
     */
    public void setLimitAmount(Double limitAmount) {
        this.limitAmount = limitAmount;
        }
    }
