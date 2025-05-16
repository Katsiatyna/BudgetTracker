package dev.katsiaryna.budgettracker.User;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller responsible for user authentication, registration, and session handling.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService; //Making sure that it works like a pipeline (controller -> service -> repository)
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor injection for dependencies.
     */
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user with provided details.
     *
     * @param username the chosen username
     * @param password the chosen password
     * @param email optional email
     * @param dob optional date of birth
     * @return HTTP response indicating success or failure
     */
    @PostMapping("/register") //Mapping the register page (Post requests are used to send data to the server)
    public ResponseEntity<String> registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String dob
    ) {
        System.out.println("Received registration request for user" + username); //Add debugging logs for each essential step to better analyze at what point our program could stop working properly using the console
         try { //try-catch block that checks if the user already exists. If yes, it will redirect us to the login page
            userService.registerUser(username, password, email, dob);
            System.out.println("User registered successfully");

            return ResponseEntity.ok()
                    .header("HX-Redirect", "/login")
                    .body("User registered successfully. Redirecting to login"); //Redirect to a login page
         } catch (Exception e) { //If the user doesn't exist, the system returns an error (most probably runtime error, nut we try to catch all possible errors)
            System.err.println("Registration failed " + e.getMessage()); //Returns the error message, according to the exception type
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Registration failed: " + e.getMessage()); //Return an error message
        }
    }


    /**
     * Authenticates a user and starts a session.
     *
     * @param username the username
     * @param password the password
     * @param request HTTP request to hold the session
     * @return HTTP response with redirect header or error
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Store authentication in session explicitly
            request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            System.out.println("Authenticated User: " + authentication.getName());
            System.out.println("Authorities: " + authentication.getAuthorities());

            return ResponseEntity.ok()
                    .header("HX-Redirect", "/home")
                    .body("Login successful!");

        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }


    /**
     * Allows password reset using a fixed global matriculation number.
     *
     * @param request the reset request containing matriculation number, username, and new password
     * @return HTTP response based on outcome
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        String hardcodedMatriculation = "102203452";

        if (!hardcodedMatriculation.equals(request.getMatriculationNumber())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid matriculation number.");
        }

        Optional<User> userOptional = userService.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        userService.updatePassword(userOptional.get(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successful.");
    }

    /**
     * Invalidates the current session (logout).
     *
     * @param request HTTP request
     * @param response HTTP response
     * @return redirect response
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate(); // Invalidate session
        return ResponseEntity.ok()
                .header("HX-Redirect", "/login") // Tell HTMX to redirect
                .body("Logout successful!");
    }

    /**
     * Utility endpoint to check if a user is authenticated.
     *
     * @return Authenticated user info or unauthorized status
     */
    @GetMapping("/check-auth")
    public ResponseEntity<String> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        return ResponseEntity.ok("Authenticated as: " + auth.getName());
    }

    /**
     * Retrieves user data by ID.
     *
     * @param id the user ID
     * @return User or 404 if not found
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates a user's details.
     *
     * @param id the user ID
     * @param updatedUser updated user information
     * @return success or error message
     */
    @PutMapping("/users/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser)
                .map(user -> ResponseEntity.ok("User updated successfully"))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

}


