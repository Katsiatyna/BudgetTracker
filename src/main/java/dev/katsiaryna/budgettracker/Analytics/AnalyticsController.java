package dev.katsiaryna.budgettracker.Analytics;

import dev.katsiaryna.budgettracker.Budget.BudgetService;
import dev.katsiaryna.budgettracker.Income.Income;
import dev.katsiaryna.budgettracker.Income.IncomeRepository;
import dev.katsiaryna.budgettracker.Income.IncomeService;
import dev.katsiaryna.budgettracker.Outcome.Outcome;
import dev.katsiaryna.budgettracker.Outcome.OutcomeRepository;
import dev.katsiaryna.budgettracker.Outcome.OutcomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for providing dashboard analytics:
 * - Summary budget overview
 * - Latest transactions
 * - Incomes/outcomes by date (line & bar)
 * - Incomes/outcomes by category (pie)
 * - Top 5 spending categories
 */
@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final IncomeService incomeService;
    private final OutcomeService outcomeService;
    private final BudgetService budgetService;
    private final IncomeRepository incomeRepository;
    private final OutcomeRepository outcomeRepository;

    /**
     * Constructor with required services and repositories.
     */
    public AnalyticsController(
            IncomeService incomeService,
            OutcomeService outcomeService,
            BudgetService budgetService,
            IncomeRepository incomeRepository,
            OutcomeRepository outcomeRepository
    ) {
        this.incomeService = incomeService;
        this.outcomeService = outcomeService;
        this.budgetService = budgetService;
        this.incomeRepository = incomeRepository;
        this.outcomeRepository = outcomeRepository;
    }

    /**
     * Returns a summary of total income, total outcome, current budget, and total category limits.
     */
    @GetMapping("/summary/{userId}")
    public Map<String, Object> getSummary(@PathVariable Long userId) {
        double totalIncome = incomeService.getIncomesByUser(userId)
                .stream().mapToDouble(Income::getAmount).sum();
        double totalOutcome = outcomeService.getOutcomesByUser(userId)
                .stream().mapToDouble(Outcome::getAmount).sum();
        double totalLimit = budgetService.getTotalLimitForUser(userId);

        Map<String, Object> summary = new HashMap<>();
        summary.put("income", totalIncome);
        summary.put("outcome", totalOutcome);
        summary.put("budget", totalIncome + totalOutcome); // outcomes stored as negative
        summary.put("limit", totalLimit);
        return summary;
    }

    /**
     * Returns the 5 most recent income and outcome transactions for a user.
     */
    @GetMapping("/latest/{userId}")
    public Map<String, List<?>> getLatestTransactions(@PathVariable Long userId) {
        List<Income> latestIncomes = incomeService.getIncomesByUser(userId)
                .stream().sorted(Comparator.comparing(Income::getDate).reversed())
                .limit(5).toList();

        List<Outcome> latestOutcomes = outcomeService.getOutcomesByUser(userId)
                .stream().sorted(Comparator.comparing(Outcome::getDate).reversed())
                .limit(5).toList();

        Map<String, List<?>> data = new HashMap<>();
        data.put("incomes", latestIncomes);
        data.put("outcomes", latestOutcomes);
        return data;
    }

    /**
     * Returns category-wise income and outcome totals for a user within a date range (for pie charts).
     */
    private Map<String, Double> convertToMap(List<Object[]> results) {
        Map<String, Double> map = new HashMap<>();
        for (Object[] row : results) {
            String category = (String) row[0];
            Double total = ((Number) row[1]).doubleValue();
            map.put(category, total);
        }
        return map;
    }

    @GetMapping("/category-summary/{userId}")
    public Map<String, Map<String, Double>> getCategorySummary(
            @PathVariable Long userId,
            @RequestParam String from,
            @RequestParam String to) {

        LocalDate start = LocalDate.parse(from);
        LocalDate end = LocalDate.parse(to);

        Map<String, Double> incomeByCategory = incomeService.getIncomesByUser(userId).stream()
                .filter(i -> !i.getDate().isBefore(start) && !i.getDate().isAfter(end))
                .collect(Collectors.groupingBy(
                        i -> i.getCategory().getCategory(),
                        Collectors.summingDouble(i -> Math.abs(i.getAmount()))
                ));

        Map<String, Double> outcomeByCategory = outcomeService.getOutcomesByUser(userId).stream()
                .filter(o -> !o.getDate().isBefore(start) && !o.getDate().isAfter(end))
                .collect(Collectors.groupingBy(
                        o -> o.getCategory().getCategory(),
                        Collectors.summingDouble(o -> Math.abs(o.getAmount()))
                ));

        Map<String, Map<String, Double>> result = new HashMap<>();
        result.put("income", incomeByCategory);
        result.put("outcome", outcomeByCategory);
        return result;
    }


    /**
     * Returns income and outcome values grouped by day/month/year (for line and bar charts).
     */
    @GetMapping("/chart")
    public Map<String, Object> getChartData(
            @RequestParam Long userId,
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String groupBy
    ) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);

        List<Income> incomes = incomeService.getIncomesByUser(userId)
                .stream()
                .filter(i -> !i.getDate().isBefore(fromDate) && !i.getDate().isAfter(toDate))
                .toList();

        List<Outcome> outcomes = outcomeService.getOutcomesByUser(userId)
                .stream()
                .filter(o -> !o.getDate().isBefore(fromDate) && !o.getDate().isAfter(toDate))
                .toList();

        Map<String, Double> incomeMap = new TreeMap<>();
        Map<String, Double> outcomeMap = new TreeMap<>();

        for (Income i : incomes) {
            String key = getGroupingKey(i.getDate(), groupBy);
            incomeMap.put(key, incomeMap.getOrDefault(key, 0.0) + i.getAmount());
        }

        for (Outcome o : outcomes) {
            String key = getGroupingKey(o.getDate(), groupBy);
            outcomeMap.put(key, outcomeMap.getOrDefault(key, 0.0) + o.getAmount());
        }

        Set<String> allKeys = new TreeSet<>();
        allKeys.addAll(incomeMap.keySet());
        allKeys.addAll(outcomeMap.keySet());

        List<String> labels = new ArrayList<>(allKeys);
        List<Double> incomeValues = labels.stream().map(l -> incomeMap.getOrDefault(l, 0.0)).toList();
        List<Double> outcomeValues = labels.stream().map(l -> outcomeMap.getOrDefault(l, 0.0)).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("incomes", incomeValues);
        result.put("outcomes", outcomeValues);
        return result;
    }

    /**
     * Groups dates by day, month, or year to produce chart labels.
     */
    private String getGroupingKey(LocalDate date, String groupBy) {
        return switch (groupBy.toLowerCase()) {
            case "month" -> date.getYear() + "-" + String.format("%02d", date.getMonthValue());
            case "year" -> String.valueOf(date.getYear());
            default -> date.toString();
        };
    }

    /**
     * Returns the total amount spent by a user in a specific category.
     */
    @GetMapping("/spent/{userId}/{categoryId}")
    public ResponseEntity<Double> getSpentForCategory(@PathVariable Long userId, @PathVariable Long categoryId) {
        double spent = outcomeService.getTotalSpentByUserAndCategory(userId, categoryId);
        return ResponseEntity.ok(spent);
    }

    /**
     * Returns the top 5 spending categories within the selected date range.
     */
    @GetMapping("/top-spendings/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getTopSpendings(
            @PathVariable Long userId,
            @RequestParam String from,
            @RequestParam String to
    ) {
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);

        List<Object[]> rawResults = outcomeRepository.findTopSpendingCategoriesByUserAndDate(userId, startDate, endDate);

        List<Map<String, Object>> results = new ArrayList<>();
        for (Object[] row : rawResults) {
            Map<String, Object> map = new HashMap<>();
            map.put("category", (String) row[0]);  // category name
            map.put("amount", ((Number) row[1]).doubleValue());  // total amount
            results.add(map);
        }

        return ResponseEntity.ok(results);
    }


}
