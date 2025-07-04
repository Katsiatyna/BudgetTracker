package dev.katsiaryna.budgettracker;

import dev.katsiaryna.budgettracker.User.User;
import dev.katsiaryna.budgettracker.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller responsible for rendering Thymeleaf pages (HTML views).
 * Handles user navigation across the home, login, register, and password reset pages.
 */
@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Landing page mapping ("/").
     * @return index view.
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    /**
     * Maps to the registration page ("/register").
     * @return register form view.
     */
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register/index";
    }

    /**
     * Maps to the login page ("/login").
     * @return login form view.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login/index";
    }

    /**
     * Maps to the forgot-password page ("/forgot-password").
     * @return forgot-password form view.
     */
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password/index";
    }

    /**
     * Loads the authenticated user's home dashboard ("/home").
     * Adds the username, user ID, and current date/time to the model for display.
     *
     * @param model Spring MVC model object
     * @return home view if authenticated, else redirect to login
     */
    @GetMapping("/home")
    public String showHomePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("username", username);
        model.addAttribute("userId", user.getId());

        // Format and display current timestamp
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        model.addAttribute("currentDateTime", formattedDateTime);

        return "home"; // Renders home.html
        }
    }
