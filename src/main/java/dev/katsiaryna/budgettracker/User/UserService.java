package dev.katsiaryna.budgettracker.User;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserService provides business logic for user registration, authentication, and account updates.
 * Implements {@link UserDetailsService} to integrate with Spring Security.
 */
@Service //This means that this class is a service, so it is the business logic of the app
public class UserService implements UserDetailsService {
    private final UserRepository userRepository; //final means that once we create it, we cannot change it anymore
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by username for authentication purposes.
     *
     * @param username the username
     * @return the UserDetails used by Spring Security
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles("USER") // Ensures a role is assigned
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Registers a new user.
     *
     * @param username the desired username
     * @param password the raw password
     * @param email optional email
     * @param dob optional date of birth (not stored)
     * @return the created User
     */
    @Transactional //Ensures the db transaction commits
    public User registerUser(String username, String password, String email, String dob) {
        System.out.println("Checking if user exists in DB: " + username);
        if (userRepository.findByUsername(username).isPresent()) { //Check if user already exists
            throw new RuntimeException("User with username " + username + " already exists"); //If it does, the system returns an error
        }

        // Generate default email if not provided - budgettracker(1/2/3 etc.)@gmail.com
        if (email == null || email.trim().isEmpty()) {
            long count = userRepository.count() + 1;
            email = "budgettracker" + count + "@gmail.com";
        }


        // Encode the password
        String encodedPassword = passwordEncoder.encode(password);

        // Create and save the new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setEmail(email);

        System.out.println("Saving user to the Database: " + username);
        user=userRepository.save(user); //Save user
        System.out.println("User saved successfully with ID");
        return user; //Saving the user
    }

    /**
     * Authenticates a user manually using username and password.
     *
     * @param username username to check
     * @param password raw password
     * @return Optional of User if authentication succeeds, otherwise empty
     */
    public Optional<User> authenticateUser(String username, String password) { //Check if the user exists (if the username and password match)
        System.out.println("Authenticating user:" +username);

        Optional<User> user = userRepository.findByUsername(username); //Checks if the user exists (if the username and password match
        if (user.isEmpty()) { //If it does not exist, the system returns an error
            System.out.println("User not found: " + username);
            return Optional.empty(); //Returns empty to allow AuthController to handle errors properly
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) { //If the password does not match, the system returns an error
            System.out.println("Incorrect password for user: " + username);
            return Optional.empty(); //Returns empty to allow AuthController to handle errors properly
        }

        System.out.println("User authenticated successfully: " + username);
        return user;
    }

    /**
     * Stub for password reset notification logic.
     *
     * @param email user email
     */
    public void sendPasswordResetEmail(String email) {
        Optional<User> user = userRepository.findByUsername(email);
        if (user.isEmpty()) {
            throw new RuntimeException("User with this email not found.");
        }

        System.out.println("Sending password reset email to: " + email);
    }

    /**
     * Updates user account fields and re-authenticates them with new credentials.
     *
     * @param id user ID
     * @param updatedData user with new data
     * @return Optional of updated user
     */
    @Transactional
    public Optional<User> updateUser(Long id, User updatedData) {
        return userRepository.findById(id).map(existing -> {
            existing.setUsername(updatedData.getUsername());
            existing.setPassword(passwordEncoder.encode(updatedData.getPassword()));
            existing.setEmail(updatedData.getEmail());
            User saved = userRepository.save(existing);

            // Re-authenticate to update session
            Authentication newAuth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            updatedData.getUsername(),
                            updatedData.getPassword() // raw password
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            return saved;
        });
    }

    /**
     * Updates the password for a given user.
     *
     * @param user user whose password is to be updated
     * @param newPassword raw new password
     */
    @Transactional
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Finds a user by ID.
     *
     * @param id user ID
     * @return Optional of User
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Finds a user by username.
     *
     * @param username user username
     * @return Optional of User
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
