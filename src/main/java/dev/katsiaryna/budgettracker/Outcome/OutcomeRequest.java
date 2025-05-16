package dev.katsiaryna.budgettracker.Outcome;

import java.time.LocalDate;

/**
 * DTO for capturing outcome data sent from the frontend when creating or updating an outcome.
 */
public class OutcomeRequest {

    private Long userId;
    private Long categoryId;
    private Double amount;
    private String description;
    private LocalDate date;

    //Getters and Setters
    /**
     * Gets the user ID associated with the outcome.
     *
     * @return userId - ID of the user
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Gets the category ID for the outcome.
     *
     * @return categoryId - ID of the category
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Gets the amount of the outcome.
     *
     * @return amount - outcome amount (should be a positive number, stored as negative in DB)
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Gets the description of the outcome.
     *
     * @return description - short explanation or note about the outcome
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the date when the outcome occurred.
     *
     * @return date of the transaction
     */
    public LocalDate getDate() {
        return date;
    }


    /**
     * Sets the user ID associated with the outcome.
     *
     * @param userId ID of the user
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Sets the category ID for the outcome.
     *
     * @param categoryId ID of the category
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Sets the amount of the outcome.
     *
     * @param amount outcome amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Sets the description of the outcome.
     *
     * @param description short note or detail about the expense
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Sets the date when the outcome occurred.
     *
     * @param date date of the transaction
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
