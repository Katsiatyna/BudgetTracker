package dev.katsiaryna.budgettracker.Category;


import jakarta.persistence.*;
import lombok.Getter;

/**
 * Represents a financial category (e.g., Food, Rent, Salary).
 * This entity is used for organizing both incomes and outcomes.
 */
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    @Column(name = "category_name", nullable = false, unique = true)
    private String category;

    @Column
    private String description;

    /**
     * Default constructor for JPA.
     */
    public Category() { }

    /**
     * Constructor with name and description.
     *
     * @param category    the category name
     * @param description optional description of the category
     */
    public Category(String category, String description) {
        this.category = category;
        this.description = description;
    }

    /**
     * Full constructor with ID, name, and description.
     *
     * @param category_id the category ID
     * @param category    the category name
     * @param description optional description
     */
    public Category(Long category_id, String category, String description) {
        this.category_id = category_id;
        this.category = category;
        this.description = description;
    }

    /**
     * Constructor with only ID (used for lightweight reference).
     *
     * @param category_id the ID of the category
     */
    public Category(Long category_id) {
        this.category_id = category_id;
    }

    //Getters
    /**
     * Gets the unique ID of the category.
     *
     * @return the ID of the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the name of the category (e.g., "Groceries", "Rent").
     *
     * @return the category name
     */
    public Long getCategory_id() {
        return category_id;
    }

    /**
     * Gets the optional description of the category.
     *
     * @return the category description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the name of the category.
     *
     * @param category the category name to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Sets the unique ID of the category.
     *
     * @param category_id the ID to set
     */
    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    /**
     * Sets the description for the category.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    // toString Method (debug-friendly string representation)
    @Override
    public String toString() {
        return "Category{" +
                "category_id=" + category_id +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
