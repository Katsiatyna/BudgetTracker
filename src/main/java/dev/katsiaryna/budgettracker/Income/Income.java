package dev.katsiaryna.budgettracker.Income;

import dev.katsiaryna.budgettracker.Category.Category;
import dev.katsiaryna.budgettracker.User.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entity representing an income transaction for a user.
 */
@Entity
@Table(name="incomes")
public class Income {

    /** Unique identifier for the income entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incomeId;

    /** The user who owns this income */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** The category associated with this income */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /** The income amount */
    @Column(nullable = false)
    private Double amount;

    /** A short description or label for the income */
    @Column(nullable = false)
    private String description;

    /** The date the income was received */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * Default constructor.
     */
    public Income() {

    }

    /**
     * Parameterized constructor to initialize an income entry.
     *
     * @param user the user who received the income
     * @param category the category of the income
     * @param amount the income amount
     * @param description short description of the income
     * @param date the date the income was received
     */
    public Income(User user, Category category, Double amount, String description, LocalDate date) {
        this.user = user;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    //Getters
    /** @return the ID of the income */
    public long getIncomeId() {
        return incomeId;
    }

    /** @return the user associated with the income */
    public User getUser() {
        return user;
    }

    /** @return the category of the income */
    public Category getCategory() {
        return category;
    }

    /** @return the amount of income */
    public Double getAmount() {
        return amount;
    }

    /** @return the description of the income */
    public String getDescription() {
        return description;
    }

    /** @return the date of the income */
    public LocalDate getDate() {
        return date;
    }

    //Setters
    /** @param incomeId the ID to set */
    public void setIncomeId(long incomeId) {
        this.incomeId = incomeId;
    }

    /** @param user the user to associate with the income */
    public void setUser(User user) {
        this.user = user;
    }

    /** @param category the category to assign to the income */
    public void setCategory(Category category) {
        this.category = category;
    }

    /** @param amount the amount to assign */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /** @param description the description to assign */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @param date the date to assign */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns a readable string representation of the income object.
     *
     * @return string containing income details
     */
    public String toString() {
        return "Income{" +
                "incomeId=" + incomeId +
                ", user=" + user +
                ", category=" + category.getCategory() +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }

}