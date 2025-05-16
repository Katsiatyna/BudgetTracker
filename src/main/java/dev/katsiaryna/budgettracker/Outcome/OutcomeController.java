package dev.katsiaryna.budgettracker.Outcome;

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
 * REST controller for managing Outcome (expenses) related operations.
 */
@RestController
@RequestMapping("/api/v1/outcomes")
public class OutcomeController {

    private final OutcomeService outcomeService;
    private final CategoryRepository categoryRepository;

    /**
     * Constructor to inject required services.
     *
     * @param outcomeService      Service for outcome logic.
     * @param categoryRepository  Repository to manage categories.
     */
    public OutcomeController(OutcomeService outcomeService, CategoryRepository categoryRepository) {
        this.outcomeService = outcomeService;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Adds a new outcome entry.
     *
     * @param request OutcomeRequest DTO with user, category, amount, description, and date.
     * @return Response message about the outcome creation.
     */
    @PostMapping
    public ResponseEntity<String> addOutcome(@RequestBody OutcomeRequest request) {
       try{
            // Fetch category from DB
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            // Call service to add outcome
            outcomeService.addOutcome(
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
     * Updates an existing outcome by ID.
     *
     * @param id      ID of the outcome to update.
     * @param request OutcomeRequest DTO with updated data.
     * @return Response message about the outcome update.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateOutcome(@PathVariable Long id, @RequestBody OutcomeRequest request) {
        try {
            outcomeService.updateOutcome(
                    id,
                    request.getCategoryId(),
                    request.getAmount(),
                    request.getDescription(),
                    request.getDate()
            );
            return ResponseEntity.ok("Outcome updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update outcome: " + e.getMessage());
        }
    }

    /**
     * Returns all outcomes in the system.
     */
    @GetMapping
    public ResponseEntity<List<Outcome>> getAllOutcomes() {
        return ResponseEntity.ok(outcomeService.getAllOutcomes());
    }

    /**
     * Returns an outcome by ID.
     *
     * @param id ID of the outcome to fetch.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Outcome> getOutcomeById(@PathVariable Long id) {
        return outcomeService.getOutcomeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Returns all outcomes for a given user.
     *
     * @param userId ID of the user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Outcome>> getOutcomesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(outcomeService.getOutcomesByUser(userId));
    }

    /**
     * Deletes an outcome by ID.
     *
     * @param id ID of the outcome to delete.
     * @return Response message about the deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOutcome(@PathVariable Long id) {
        try {
            outcomeService.deleteOutcome(id);
            return ResponseEntity.ok("Outcome deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete outcome: " + e.getMessage());
        }
    }

    /**
     * Exports outcomes to CSV file for a specific user within a given date range.
     *
     * @param userId   ID of the user.
     * @param from     Start date of export range.
     * @param to       End date of export range.
     * @param response HttpServletResponse for writing CSV content.
     */
    @GetMapping("/export")
    public void exportOutcomesToCsv(
            @RequestParam Long userId,
            @RequestParam String from,
            @RequestParam String to,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=outcomes.csv");

        List<Outcome> outcomes = outcomeService.getOutcomesByUser(userId).stream()
                .filter(o -> !o.getDate().isBefore(LocalDate.parse(from)) &&
                        !o.getDate().isAfter(LocalDate.parse(to)))
                .toList();

        PrintWriter writer = response.getWriter();
        writer.println("Date,Amount,Description,Category");

        for (Outcome outcome : outcomes) {
            writer.printf("%s,%.2f,%s,%s%n",
                    outcome.getDate(),
                    outcome.getAmount(),
                    outcome.getDescription(),
                    outcome.getCategory().getCategory());
        }
        writer.flush();
    }



}
