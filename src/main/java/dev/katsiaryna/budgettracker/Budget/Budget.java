package dev.katsiaryna.budgettracker.Budget;

import dev.katsiaryna.budgettracker.Category.Category;
import dev.katsiaryna.budgettracker.User.User;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Represents a budget limit for a specific category assigned to a user.
 * Each budget entry associates a user with a category and defines a spending limit.
 */
@Entity
@Table(name = "budgets")
public class Budget {

    /**
     * Unique identifier for the budget entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    /**
     * The category this budget is assigned to.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * The user this budget belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The spending limit amount for the category.
     */
    private Double limitAmount;

    /**
     * Default no-args constructor required by JPA.
     */
    public Budget() {
    }

    /**
     * Constructor for creating a budget entry with user, category, and limit.
     *
     * @param user        the user this budget belongs to
     * @param category    the category for this budget
     * @param limitAmount the spending limit amount
     */
    public Budget(User user, Category category, Double limitAmount) {
        this.user = user;
        this.category = category;
        this.limitAmount = limitAmount;
    }

    // Getters
    /**
     * @return the ID of the budget
     */
    public Long getBudgetId() {
        return budgetId;
    }

    /**
     * @return the user associated with this budget
     */
    public User getUser() {
        return user;
    }

    /**
     * @return the category associated with this budget
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @return the limit amount set for this budget
     */
    public Double getLimitAmount() {
        return limitAmount;
    }

    // Setters
    /**
     * @param budgetId the ID to set for this budget
     */
    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }

    /**
     * @param user the user to associate with this budget
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @param category the category to associate with this budget
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @param limitAmount the limit amount to set for this budget
     */
    public void setLimitAmount(Double limitAmount) {
        this.limitAmount = limitAmount;
    }
}