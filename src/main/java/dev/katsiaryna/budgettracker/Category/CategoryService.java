package dev.katsiaryna.budgettracker.Category;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class that provides business logic for managing categories.
 * It handles operations such as adding, retrieving, and deleting categories.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Constructor to inject the CategoryRepository.
     *
     * @param categoryRepository the repository for category data
     */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Adds a new category if it does not already exist.
     *
     * @param categoryName the name of the new category
     * @param description  an optional description of the category
     * @return the created Category object
     * @throws RuntimeException if a category with the same name already exists
     */
    public Category addCategory(String categoryName, String description) {
        if (categoryRepository.findByCategory(categoryName).isPresent()) {
            throw new RuntimeException("Category with name " + categoryName + " already exists");
        }

        // Sanitize optional description
        String categoryDescription = (description == null || description.trim().isEmpty()) ? null : description;

        Category category = new Category(categoryName, categoryDescription);
        return categoryRepository.save(category);
    }

    /**
     * Retrieves all categories in the system.
     *
     * @return a list of Category objects
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Retrieves a single category by its ID.
     *
     * @param id the ID of the category
     * @return an Optional containing the category if found
     */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     */
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
