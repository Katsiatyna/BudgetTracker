package dev.katsiaryna.budgettracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point of the Budget Tracker application.
 * <p>
 * This class bootstraps the Spring Boot application by enabling
 * component scanning, configuration, and auto-configuration.
 * </p>
 */
@SpringBootApplication
public class BudgetTrackerApplication {

	/**
	 * Main method that starts the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(BudgetTrackerApplication.class, args);
	}
}
