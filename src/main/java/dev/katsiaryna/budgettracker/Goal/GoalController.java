package dev.katsiaryna.budgettracker.Goal;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user financial goals.
 * Provides endpoints for creating, retrieving, updating, and deleting goals.
 */
@RestController
@RequestMapping("/api/v1/goals")
public class GoalController {

    private final GoalService goalService;

    /**
     * Constructor for GoalController.
     *
     * @param goalService the service to manage goal logic
     */
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    /**
     * Retrieves all goals for a specific user.
     *
     * @param userId the ID of the user
     * @return list of goals owned by the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Goal>> getGoalsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(goalService.getGoalsByUser(userId));
    }

    /**
     * Creates a new goal.
     *
     * @param goal the goal to be added
     * @return the created goal
     */
    @PostMapping
    public ResponseEntity<Goal> addGoal(@RequestBody Goal goal) {
        return ResponseEntity.ok(goalService.addGoal(goal));
    }

    /**
     * Deletes a goal by its ID.
     *
     * @param goalId the ID of the goal to delete
     * @return success message if deleted
     */
    @DeleteMapping("/{goalId}")
    public ResponseEntity<String> deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
        return ResponseEntity.ok("Goal deleted successfully");
    }

    /**
     * Updates the current amount (progress) of a specific goal.
     *
     * @param id            the ID of the goal to update
     * @param currentAmount the new current amount value
     * @return the updated goal if found, or 404 if not
     */
    @PutMapping("/progress/{id}")
    public ResponseEntity<Goal> updateProgress(@PathVariable Long id, @RequestBody Double currentAmount) {
        return goalService.updateProgress(id, currentAmount)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
