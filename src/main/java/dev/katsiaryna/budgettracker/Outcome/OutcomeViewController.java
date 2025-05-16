package dev.katsiaryna.budgettracker.Outcome;

import dev.katsiaryna.budgettracker.User.User;
import dev.katsiaryna.budgettracker.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to render the outcome (expenses) entry page using Thymeleaf.
 * This controller fetches the authenticated user's information and passes it to the frontend.
 */
@Controller
public class OutcomeViewController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Displays the outcome page for the currently authenticated user.
     * It adds the username and userId to the Thymeleaf model for frontend rendering.
     *
     * @param model Thymeleaf model used to pass data to the view
     * @return name of the Thymeleaf HTML template, or redirect to login page if unauthenticated
     */
    @GetMapping("/outcome")
    public String showOutcomePage(Model model) {
        // Fetch the authentication from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Ensure the user is authenticated before proceeding
        if (authentication == null) {
            System.out.println("Authentication is null. Redirecting to login.");
            return "redirect:/login";
        }
        if (!authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            System.out.println("User is not authenticated. Redirecting to login.");
            return "redirect:/login";
        }

        // Fetch username from Spring Security context
        String username = authentication.getName();
        model.addAttribute("username", username);

        // Retrieve user ID from database and pass it to the view
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("userId", user.getId());

        // Render the outcome/index.html page
        return "outcome/index";
    }

}
