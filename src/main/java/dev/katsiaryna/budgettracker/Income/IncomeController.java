package dev.katsiaryna.budgettracker.Income;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dev.katsiaryna.budgettracker.Category.Category;
import dev.katsiaryna.budgettracker.Category.CategoryRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing income operations.
 */
@RestController
@RequestMapping("/api/v1/incomes")
public class IncomeController {

    private final IncomeService incomeService;
    private final CategoryRepository categoryRepository;

    /**
     * Constructor for injecting service and repository dependencies.
     */
    public IncomeController(IncomeService incomeService, CategoryRepository categoryRepository) {
        this.incomeService = incomeService;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Constructor for injecting service and repository dependencies.
     */
    @PostMapping
    public ResponseEntity<String> addIncome(@RequestBody IncomeRequest request) {
        try {
            // Fetch category from DB
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            // Call service to add income
            incomeService.addIncome(
                    request.getUserId(),
                    category.getCategory_id(),
                    request.getAmount(),
                    request.getDescription(),
                    request.getDate()
            );
            return ResponseEntity.ok("Income added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add income: " + e.getMessage());
        }
    }

    /**
     * Updates an existing income entry.
     *
     * @param id      the ID of the income
     * @param request the updated income data
     * @return success or error response
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateIncome(@PathVariable Long id, @RequestBody IncomeRequest request) {
        try {
            incomeService.updateIncome(
                    id,
                    request.getCategoryId(),
                    request.getAmount(),
                    request.getDescription(),
                    request.getDate()
            );
            return ResponseEntity.ok("Income updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update income: " + e.getMessage());
        }
    }


    /**
     * Retrieves all income records in the system.
     *
     * @return list of all incomes
     */
    @GetMapping
    public ResponseEntity<List<Income>> getAllIncomes() {
        return ResponseEntity.ok(incomeService.getAllIncomes());
    }

    /**
     * Retrieves an income record by its ID.
     *
     * @param id the income ID
     * @return income details or 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Income> getIncomeById(@PathVariable Long id) {
        return incomeService.getIncomeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all incomes for a given user.
     *
     * @param userId the user ID
     * @return list of incomes
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Income>> getIncomesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(incomeService.getIncomesByUser(userId));
    }

    /**
     * Deletes an income by its ID.
     *
     * @param id the income ID
     * @return success or failure message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIncome(@PathVariable Long id) {
        try {
            incomeService.deleteIncome(id);
            return ResponseEntity.ok("Income deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete income: " + e.getMessage());
        }
    }

    /**
     * Exports incomes to CSV for a given user and date range.
     *
     * @param userId   the user ID
     * @param from     start date (inclusive)
     * @param to       end date (inclusive)
     * @param response the servlet response
     * @throws IOException if writing to output fails
     */
    @GetMapping("/export")
    public void exportIncomesToCsv(
            @RequestParam Long userId,
            @RequestParam String from,
            @RequestParam String to,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=incomes.csv");

        List<Income> incomes = incomeService.getIncomesByUser(userId).stream()
                .filter(i -> !i.getDate().isBefore(LocalDate.parse(from)) &&
                        !i.getDate().isAfter(LocalDate.parse(to)))
                .toList();

        PrintWriter writer = response.getWriter();
        writer.println("Date,Amount,Description,Category");

        for (Income income : incomes) {
            writer.printf("%s,%.2f,%s,%s%n",
                    income.getDate(),
                    income.getAmount(),
                    income.getDescription(),
                    income.getCategory().getCategory());
        }
        writer.flush();
    }


}