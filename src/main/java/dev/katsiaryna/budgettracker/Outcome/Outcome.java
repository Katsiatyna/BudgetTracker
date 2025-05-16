package dev.katsiaryna.budgettracker.Outcome;

import dev.katsiaryna.budgettracker.Category.Category;
import dev.katsiaryna.budgettracker.User.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entity class representing an outcome (expense) record.
 */
@Entity
@Table(name="outcomes")
public class Outcome {

    /** Unique identifier for the outcome entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outcomeId;

    /** The user who owns this outcome */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** The category of the outcome */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /** The amount spent */
    @Column(nullable = false)
    private Double amount;

    /** Description of the outcome */
    @Column(nullable = false)
    private String description;

    /** Date of the outcome */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * Parameterized constructor for creating an Outcome instance.
     *
     * @param user The user who logged the outcome.
     * @param category The category of the outcome.
     * @param amount The amount spent.
     * @param description Description of the outcome.
     * @param date Date of the outcome.
     */
    public Outcome(User user, Category category, Double amount, String description, LocalDate date) {
        this.user = user;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    /**
     * Default constructor.
     */
    public Outcome() {

    }

    // Getters
    /**
     * @return the outcome ID
     */
    public Long getOutcomeId() {
        return outcomeId;
    }

    /**
     * @return the user who recorded the outcome
     */
    public User getUser() {
        return user;
    }

    /**
     * @return the category of the outcome
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @return the amount of the outcome
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @return the description of the outcome
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the date of the outcome
     */
    public LocalDate getDate() {
        return date;
    }

    // Setters
    /**
     * @param user Sets the user who logged the outcome
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @param category Sets the category of the outcome
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @param amount Sets the amount of the outcome
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * @param description Sets the description of the outcome
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param date Sets the date of the outcome
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return A string representation of the outcome object
     */
    public String toString() {
        return "Outcome{" +
                "outcomeId=" + outcomeId +
                ", user=" + user +
                ", category=" + category.getCategory() +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
