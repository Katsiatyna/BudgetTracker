package dev.katsiaryna.budgettracker.User;

/**
 * DTO (Data Transfer Object) for handling password reset requests.
 * This class is used to capture the necessary information from the user
 * to perform a secure password reset via a hardcoded matriculation number.
 */
public class PasswordResetRequest {

    /**
     * The matriculation number used for verification.
     * This is a fixed value shared across the application, acting as a global reset token.
     */
    private String matriculationNumber;

    /**
     * The username of the user requesting the password reset.
     */
    private String username;

    /**
     * The new password the user wants to set.
     */
    private String newPassword;

    // Getters
    /**
     * Gets the matriculation number used for verification.
     * @return the matriculation number
     */
    public String getMatriculationNumber() {
        return matriculationNumber;
    }

    /**
     * Gets the username of the account whose password will be reset.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the new password to set for the user.
     * @return the new password
     */
    public String getNewPassword() {
        return newPassword;
    }

    // Setters
    /**
     * Sets the matriculation number used for verification.
     * @param matriculationNumber the matriculation number
     */
    public void setMatriculationNumber(String matriculationNumber) {
        this.matriculationNumber = matriculationNumber;
    }

    /**
     * Sets the username of the account to reset the password for.
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the new password to be applied.
     * @param newPassword the new password
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
