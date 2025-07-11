package dev.katsiaryna.budgettracker.Income;

import dev.katsiaryna.budgettracker.User.User;
import dev.katsiaryna.budgettracker.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for rendering the income view page (Thymeleaf).
 */
@Controller
public class IncomeViewController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Handles GET request to the income page.
     * Validates the authenticated user and injects their username and ID into the model.
     *
     * @param model The Spring MVC model for passing data to the Thymeleaf view.
     * @return The view name for the income page or redirects to login if not authenticated.
     */
    @GetMapping("/income")
    public String showIncomePage(Model model) {
        // Fetch the authentication from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // If user is not authenticated, redirect to login
        if (authentication == null) {
            System.out.println("Authentication is null. Redirecting to login.");
            return "redirect:/login";
        }
        if (!authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            System.out.println("User is not authenticated. Redirecting to login.");
            return "redirect:/login";
        }

        // Get the authenticated username
        String username = authentication.getName();
        model.addAttribute("username", username);

        // Look up the user entity and add their ID to the model
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("userId", user.getId());

        // Return Thymeleaf view for income
        return "income/index";
    }
}