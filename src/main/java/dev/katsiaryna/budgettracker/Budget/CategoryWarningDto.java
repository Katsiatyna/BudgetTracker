package dev.katsiaryna.budgettracker.Budget;

/**
 * Data Transfer Object (DTO) representing a warning when spending
 * reaches or exceeds a certain percentage of the budget limit.
 */
public class CategoryWarningDto {

    private String categoryName;
    private double spent;
    private double limitAmount;
    private double percentUsed;

    /**
     * Constructs a new CategoryWarningDto with provided category name,
     * amount spent, and limit.
     * Calculates the percentage used automatically.
     *
     * @param categoryName the name of the category
     * @param spent the amount spent
     * @param limitAmount the budget limit for this category
     */
    public CategoryWarningDto(String categoryName, double spent, double limitAmount) {
        this.categoryName = categoryName;
        this.spent = spent;
        this.limitAmount = limitAmount;
        this.percentUsed = (limitAmount > 0) ? (spent / limitAmount) * 100 : 0;
    }

    // Getters
    /** @return the category name */
    public String getCategoryName() {
        return categoryName;
    }

    /** @return the amount spent */
    public double getSpent() {
        return spent;
    }

    /** @return the budget limit */
    public double getLimitAmount() {
        return limitAmount;
    }

    /** @return the percentage of the limit used */
    public double getPercentUsed() {
        return percentUsed;
    }

    // Setters

    /** @param categoryName set the category name */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /** @param spent set the amount spent */
    public void setSpent(double spent) {
        this.spent = spent;
    }

    /** @param limitAmount set the budget limit */
    public void setLimitAmount(double limitAmount) {
        this.limitAmount = limitAmount;
    }

    /** @param percentUsed manually set the percentage used */
    public void setPercentUsed(double percentUsed) {
        this.percentUsed = percentUsed;
    }
}
