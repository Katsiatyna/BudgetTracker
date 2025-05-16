package dev.katsiaryna.budgettracker.User;

/**
 * Represents the incoming registration request payload.
 * Contains the user's chosen username and password.
 */
record RegisterRequest(String username, String password) {}

/**
 * Represents the response returned upon successful registration.
 * Includes the user's database ID and username.
 */
record RegisterResponse(Long id, String username) {}

/**
 * Represents the login request payload.
 * Carries the username and password needed to authenticate a user.
 */
record LoginRequest(String username, String password) {}

/**
 * Represents the login response, typically used to return a JWT token or session token.
 */
record LoginResponse(String token) {}

/**
 * Data Transfer Object (DTO) for updating or transferring user data such as credentials.
 */
record UserDTO(String username, String password) {}


