package dev.katsiaryna.budgettracker.Category;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling Category-related operations such as
 * creating, retrieving, and deleting categories.
 */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Constructor for CategoryController.
     *
     * @param categoryService the service responsible for category business logic
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Creates a new category.
     *
     * @param categoryName the name of the category
     * @param description  optional description of the category
     * @return ResponseEntity with success or error message
     */
    @PostMapping
    public ResponseEntity<String> addCategory(@RequestParam String categoryName, @RequestParam(required = false) String description) {
        try {
            // If description is empty or null, default to null
            String categoryDescription = (description == null || description.trim().isEmpty()) ? null : description;
            categoryService.addCategory(categoryName, categoryDescription);
            return ResponseEntity.ok("Category added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add category: " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of all categories.
     *
     * @return list of all categories
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * Retrieves a single category by its ID.
     *
     * @param id the ID of the category
     * @return the category if found, 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     * @return success or error message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Category deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete category: " + e.getMessage());
        }
    }

}
