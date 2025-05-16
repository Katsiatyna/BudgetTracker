package dev.katsiaryna.budgettracker.Goal;


import dev.katsiaryna.budgettracker.User.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class that handles business logic related to user goals.
 */
@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    /**
     * Constructor for injecting dependencies.
     *
     * @param goalRepository  the repository for managing Goal entities
     * @param userRepository  the repository for managing User entities
     */
    public GoalService(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all goals for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of goals associated with the user
     */
    public List<Goal> getGoalsByUser(Long userId) {
        return goalRepository.findByUser_Id(userId);
    }

    /**
     * Adds a new goal to the database.
     *
     * @param goal the goal object to save
     * @return the saved goal
     */
    public Goal addGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    /**
     * Deletes a goal by its ID.
     *
     * @param id the ID of the goal to delete
     */
    public void deleteGoal(Long id){
        goalRepository.deleteById(id);
    }

    /**
     * Updates the current amount progress of a goal.
     *
     * @param id         the ID of the goal to update
     * @param newAmount  the new current amount value
     * @return an Optional containing the updated goal, or empty if not found
     */
    public Optional<Goal> updateProgress(Long id, Double newAmount) {
        Optional<Goal> optionalGoal = goalRepository.findById(id);
        if (optionalGoal.isPresent()) {
            Goal goal = optionalGoal.get();
            goal.setCurrentAmount(newAmount);
            goalRepository.save(goal);
            return Optional.of(goal);
    }
        return Optional.empty();
    }
}
