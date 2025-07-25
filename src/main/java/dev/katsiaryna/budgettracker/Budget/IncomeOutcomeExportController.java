package dev.katsiaryna.budgettracker.Budget;

import dev.katsiaryna.budgettracker.Income.Income;
import dev.katsiaryna.budgettracker.Income.IncomeService;
import dev.katsiaryna.budgettracker.Outcome.Outcome;
import dev.katsiaryna.budgettracker.Outcome.OutcomeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Controller for exporting income and outcome records as downloadable CSV files.
 */
@RestController
@RequestMapping("/api/v1/export")
public class IncomeOutcomeExportController {

    private final IncomeService incomeService;
    private final OutcomeService outcomeService;

    /**
     * Constructor for dependency injection.
     *
     * @param incomeService  service for income-related operations
     * @param outcomeService service for outcome-related operations
     */
    public IncomeOutcomeExportController(IncomeService incomeService, OutcomeService outcomeService) {
        this.incomeService = incomeService;
        this.outcomeService = outcomeService;
    }

    /**
     * Exports all income records for a user as a CSV file.
     *
     * @param userId   the ID of the user
     * @param response the HTTP response to write the CSV output to
     * @throws IOException if an input/output error occurs
     */
    @GetMapping(value = "/incomes/{userId}", produces = "text/csv")
    public void exportIncomesToCSV(@PathVariable Long userId, HttpServletResponse response) throws IOException {
        List<Income> incomes = incomeService.getIncomesByUser(userId);

        response.setHeader("Content-Disposition", "attachment; filename=incomes.csv");
        PrintWriter writer = response.getWriter();
        writer.println("Date,Category,Amount,Description");

        for (Income i : incomes) {
            writer.printf("%s,%s,%.2f,%s%n",
                    i.getDate(),
                    i.getCategory().getCategory(),
                    i.getAmount(),
                    i.getDescription());
        }

        writer.flush();
    }

    /**
     * Exports all outcome records for a user as a CSV file.
     *
     * @param userId   the ID of the user
     * @param response the HTTP response to write the CSV output to
     * @throws IOException if an input/output error occurs
     */
    @GetMapping(value = "/outcomes/{userId}", produces = "text/csv")
    public void exportOutcomesToCSV(@PathVariable Long userId, HttpServletResponse response) throws IOException {
        List<Outcome> outcomes = outcomeService.getOutcomesByUser(userId);

        response.setHeader("Content-Disposition", "attachment; filename=outcomes.csv");
        PrintWriter writer = response.getWriter();
        writer.println("Date,Category,Amount,Description");

        for (Outcome o : outcomes) {
            writer.printf("%s,%s,%.2f,%s%n",
                    o.getDate(),
                    o.getCategory().getCategory(),
                    Math.abs(o.getAmount()),
                    o.getDescription());
        }

        writer.flush();
    }
}

