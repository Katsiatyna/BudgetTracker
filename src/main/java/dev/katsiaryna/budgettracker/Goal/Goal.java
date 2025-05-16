package dev.katsiaryna.budgettracker.Goal;


import dev.katsiaryna.budgettracker.User.User;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entity representing a financial goal for a user.
 * A goal can be categorized (e.g. University, Travel) and belong to a type such as Savings, Debt, or Mortgage.
 */
@Entity
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String type; // Expected values: "Savings", "Debt", "Mortgage"
    private String name;
    private Double targetAmount;
    private Double currentAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String goalCategory;

    /** Default constructor for JPA */
    public Goal() {}

    /**
     * Constructor with all properties.
     *
     * @param user           the owner of the goal
     * @param type           type of goal (e.g. Savings, Debt, Mortgage)
     * @param name           name/title of the goal
     * @param targetAmount   amount intended to be reached
     * @param currentAmount  amount currently saved or paid
     * @param startDate      when the goal starts
     * @param endDate        when the goal is expected to be completed
     * @param goalCategory   a category label (e.g. "University", "Vacation")
     */
    public Goal(User user, String type, String name, Double targetAmount, Double currentAmount,
                LocalDate startDate, LocalDate endDate, String goalCategory) {
        this.user = user;
        this.type = type;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goalCategory = goalCategory;
    }

    // Getters
    /** @return goal ID (primary key) */
    public Long getGoalId() {
        return goalId;
    }

    /** @return associated user */
    public User getUser() {
        return user;
    }

    /** @return goal type (Savings, Debt, or Mortgage) */
    public String getType() {
        return type;
    }

    /** @return name of the goal */
    public String getName() {
        return name;
    }

    /** @return target amount to reach */
    public Double getTargetAmount() {
        return targetAmount;
    }

    /** @return current amount saved or paid */
    public Double getCurrentAmount() {
        return currentAmount;
    }

    /** @return start date of the goal */
    public LocalDate getStartDate() {
        return startDate;
    }

    /** @return end date of the goal */
    public LocalDate getEndDate() {
        return endDate;
    }

    /** @return category of the goal */
    public String getGoalCategory() {
        return goalCategory;
    }

    // Setters

    /**
     *
     * @param goalId
     */
    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }

    /**
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param targetAmount
     */
    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    /**
     *
     * @param currentAmount
     */
    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    /**
     *
     * @param startDate
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @param endDate
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @param goalCategory
     */
    public void setGoalCategory(String goalCategory) {
        this.goalCategory = goalCategory;
    }
}
