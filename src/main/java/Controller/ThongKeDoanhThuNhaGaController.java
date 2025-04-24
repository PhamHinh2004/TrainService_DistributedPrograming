package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ThongKeDoanhThuNhaGaController implements Initializable {

    @FXML
    private ComboBox<Integer> cbMonth;
    @FXML
    private ComboBox<Integer> cbYearMonthly; // Year for monthly stats

    @FXML
    private ComboBox<Integer> cbStartYear;   // Start year for annual range stats
    @FXML
    private ComboBox<Integer> cbEndYear;     // End year for annual range stats

    @FXML
    private PieChart pieChart;

    @FXML
    private BarChart<String, Number> annualRevenueChart;
    @FXML
    private CategoryAxis xAxisAnnual;
    @FXML
    private NumberAxis yAxisAnnual;

    @FXML
    private Button btnCustomerTrend;

    // --- Mock Data Structures ---
    // Mock data for monthly revenue (Map: Year -> Map: Month -> {tickets, revenue})
    private final Map<Integer, Map<Integer, MonthlyStats>> mockMonthlyData = new HashMap<>();
    // Mock data for annual revenue (Map: Year -> {tickets, revenue})
    private final Map<Integer, AnnualStats> mockAnnualData = new HashMap<>();

    private static class MonthlyStats {
        int ticketsSold;
        double revenue;

        MonthlyStats(int ticketsSold, double revenue) {
            this.ticketsSold = ticketsSold;
            this.revenue = revenue;
        }
    }

    private static class AnnualStats {
        int totalTicketsSold;
        double totalRevenue;

        AnnualStats(int totalTicketsSold, double totalRevenue) {
            this.totalTicketsSold = totalTicketsSold;
            this.totalRevenue = totalRevenue;
        }
    }
    // --- End Mock Data Structures ---


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // --- Populate Mock Data ---
        populateMockData();
        // --- End Populate Mock Data ---

        // Populate ComboBoxes
        populateComboBoxes();

        // Set listeners for ComboBoxes
        setupListeners();

        // Set action for the button (placeholder)
        btnCustomerTrend.setOnAction(event -> handleCustomerTrend());

        // Load initial data (e.g., for the latest year or a default range)
        loadInitialData();

        // Configure chart axes (can also be done in FXML)
        xAxisAnnual.setLabel("Năm");
        yAxisAnnual.setLabel("Doanh thu (triệu VNĐ)");
       // yAxisAnnual.setTickLabelFormatter(new NumberAxis.DefaultNumberFormat());
    }

    // --- Mock Data Population ---
    private void populateMockData() {
        // Populate data for 2022, 2023, 2024
        int currentYear = Year.now().getValue();
        for (int year = 2022; year <= currentYear; year++) {
            Map<Integer, MonthlyStats> yearData = new HashMap<>();
            double totalYearRevenue = 0;
            int totalYearTickets = 0;

            for (int month = 1; month <= 12; month++) {
                // Generate some dummy data
                int tickets = 100 + (int)(Math.random() * 200) + (year - 2022) * 50; // Increase over years
                double revenue = tickets * (50 + Math.random() * 50) / 1000.0; // Revenue in millions, based on tickets
                yearData.put(month, new MonthlyStats(tickets, revenue));
                totalYearTickets += tickets;
                totalYearRevenue += revenue;
            }
            mockMonthlyData.put(year, yearData);
            mockAnnualData.put(year, new AnnualStats(totalYearTickets, totalYearRevenue));
        }
    }
    // --- End Mock Data Population ---

    private void populateComboBoxes() {
        // Populate Months (1-12)
        ObservableList<Integer> months = FXCollections.observableArrayList(IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList()));
        cbMonth.setItems(months);

        // Populate Years for monthly and annual range filters
        ObservableList<Integer> years = FXCollections.observableArrayList(mockMonthlyData.keySet());
        years.sort(Integer::compareTo); // Sort years

        cbYearMonthly.setItems(FXCollections.observableArrayList(years));
        cbStartYear.setItems(FXCollections.observableArrayList(years));
        cbEndYear.setItems(FXCollections.observableArrayList(years));

        // Select default values (e.g., current month/year or latest year range)
        int latestYear = years.isEmpty() ? Year.now().getValue() : years.get(years.size() - 1);
        int currentMonth = java.time.LocalDate.now().getMonthValue();

        if (cbMonth.getItems().contains(currentMonth)) {
            cbMonth.getSelectionModel().select(Integer.valueOf(currentMonth));
        } else if (!cbMonth.getItems().isEmpty()){
            cbMonth.getSelectionModel().selectFirst();
        }


        if (cbYearMonthly.getItems().contains(latestYear)) {
            cbYearMonthly.getSelectionModel().select(Integer.valueOf(latestYear));
        } else if (!cbYearMonthly.getItems().isEmpty()) {
            cbYearMonthly.getSelectionModel().selectLast();
        }


        if (!years.isEmpty()) {
            cbStartYear.getSelectionModel().selectFirst();
            cbEndYear.getSelectionModel().selectLast(); // Default range is all available years
        }
    }

    private void setupListeners() {
        // Listener for Monthly filters
        cbMonth.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && cbYearMonthly.getSelectionModel().getSelectedItem() != null) {
                loadMonthlyStats(newVal, cbYearMonthly.getSelectionModel().getSelectedItem());
                updatePieChart(newVal, cbYearMonthly.getSelectionModel().getSelectedItem());
            } else if (newVal == null) {
                // Handle clearing or resetting if month is unselected
                // Or perhaps do nothing if year is selected but month isn't
            }
        });

        cbYearMonthly.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && cbMonth.getSelectionModel().getSelectedItem() != null) {
                loadMonthlyStats(cbMonth.getSelectionModel().getSelectedItem(), newVal);
                updatePieChart(cbMonth.getSelectionModel().getSelectedItem(), newVal);
            } else if (newVal != null) {
                // Update pie chart even if month is not selected, maybe show overall annual?
                // Or just clear pie chart if month is needed
                clearPieChart(); // Clear if month is not selected
            } else {
                // Handle clearing or resetting if year is unselected
                clearMonthlyStatsTable(); // Placeholder action
                clearPieChart();
            }
        });

        // Listener for Annual Range filters
        cbStartYear.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && cbEndYear.getSelectionModel().getSelectedItem() != null) {
                loadAnnualRangeStats(newVal, cbEndYear.getSelectionModel().getSelectedItem());
            } else if (newVal == null) {
                clearAnnualRangeTable(); // Placeholder action
                clearAnnualRevenueChart();
            }
        });

        cbEndYear.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && cbStartYear.getSelectionModel().getSelectedItem() != null) {
                loadAnnualRangeStats(cbStartYear.getSelectionModel().getSelectedItem(), newVal);
            } else if (newVal == null) {
                clearAnnualRangeTable(); // Placeholder action
                clearAnnualRevenueChart();
            }
        });
    }

    private void loadInitialData() {
        // Load monthly stats and pie chart for the initially selected month/year
        Integer initialMonth = cbMonth.getSelectionModel().getSelectedItem();
        Integer initialYearMonthly = cbYearMonthly.getSelectionModel().getSelectedItem();
        if (initialMonth != null && initialYearMonthly != null) {
            loadMonthlyStats(initialMonth, initialYearMonthly);
            updatePieChart(initialMonth, initialYearMonthly);
        }

        // Load annual range stats and bar chart for the initially selected year range
        Integer initialStartYear = cbStartYear.getSelectionModel().getSelectedItem();
        Integer initialEndYear = cbEndYear.getSelectionModel().getSelectedItem();
        if (initialStartYear != null && initialEndYear != null) {
            loadAnnualRangeStats(initialStartYear, initialEndYear);
        }
    }


    // --- Data Loading and Display Methods (Using Mock Data) ---

    private void loadMonthlyStats(int month, int year) {
        System.out.println("Loading monthly stats for Month " + month + ", Year " + year);
        Map<Integer, MonthlyStats> yearData = mockMonthlyData.get(year);
        if (yearData != null && yearData.containsKey(month)) {
            MonthlyStats stats = yearData.get(month);
            System.out.println(String.format("Mock Monthly Data: Tickets=%d, Revenue=%.2f", stats.ticketsSold, stats.revenue));
            // TODO: Update the placeholder table area with this data
            // If you were using a TableView, you would update its items here.
            // For now, we just print to console or update a placeholder label.
        } else {
            System.out.println("No monthly data found for Month " + month + ", Year " + year);
            // TODO: Clear the placeholder table area
        }
    }

    private void updatePieChart(int month, int year) {
        System.out.println("Updating pie chart for Month " + month + ", Year " + year);
        double monthRevenue = 0;
        double annualRevenue = 0;

        Map<Integer, MonthlyStats> yearMonthlyData = mockMonthlyData.get(year);
        AnnualStats yearAnnualData = mockAnnualData.get(year);

        if (yearMonthlyData != null && yearMonthlyData.containsKey(month)) {
            monthRevenue = yearMonthlyData.get(month).revenue;
        }

        if (yearAnnualData != null) {
            annualRevenue = yearAnnualData.totalRevenue;
        }

        // Calculate the remaining revenue for the year (Annual - Month)
        double remainingAnnualRevenue = annualRevenue - monthRevenue;
        if (remainingAnnualRevenue < 0) remainingAnnualRevenue = 0; // Should not be negative

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Add data only if there's revenue to show
        if (monthRevenue > 0) {
            pieChartData.add(new PieChart.Data(String.format("Tháng %d (%.2ftr)", month, monthRevenue), monthRevenue));
        }
        if (remainingAnnualRevenue > 0) {
            pieChartData.add(new PieChart.Data(String.format("Còn lại (%.2ftr)", remainingAnnualRevenue), remainingAnnualRevenue));
        } else if (annualRevenue > 0 && monthRevenue == annualRevenue) {
            // Special case: if monthly revenue IS the annual revenue (e.g., month 12 selected)
            pieChartData.add(new PieChart.Data(String.format("Tháng %d (%.2ftr)", month, monthRevenue), monthRevenue));
        }


        pieChart.setData(pieChartData);

        // Apply custom colors based on the legend description
        // This requires looking up the data slices after setting the data
        // Note: This styling is complex and might be easier with CSS, but here's a direct way
        // Run later to ensure nodes are created
        javafx.application.Platform.runLater(() -> {
            for (final PieChart.Data data : pieChart.getData()) {
                if (data.getName().contains("Tháng")) {
                    // Style for the month slice (light gray in legend)
                    data.getNode().setStyle("-fx-pie-color: lightgray;");
                } else if (data.getName().contains("Còn lại")) {
                    // Style for the remaining year slice (black in legend)
                    data.getNode().setStyle("-fx-pie-color: black;");
                }
                // You might need to adjust legend labels or use Tooltips for clarity
            }
            // Hide default legend if using custom legend in FXML
            pieChart.setLegendVisible(false);
        });

        // If total annual revenue is 0, clear the chart
        if (annualRevenue == 0) {
            clearPieChart();
        }
    }

    private void clearPieChart() {
        pieChart.setData(FXCollections.observableArrayList()); // Clear data
    }


    private void loadAnnualRangeStats(int startYear, int endYear) {
        System.out.println("Loading annual range stats from " + startYear + " to " + endYear);

        if (startYear > endYear) {
            System.out.println("Error: Start year cannot be after end year.");
            // TODO: Display an error message to the user
            clearAnnualRangeTable();
            clearAnnualRevenueChart();
            return;
        }

        List<AnnualStats> statsList = new ArrayList<>();
        List<Integer> yearsInRange = IntStream.rangeClosed(startYear, endYear)
                .boxed()
                .collect(Collectors.toList());

        for (int year : yearsInRange) {
            AnnualStats stats = mockAnnualData.get(year);
            if (stats != null) {
                statsList.add(stats);
                System.out.println(String.format("Mock Annual Data %d: Tickets=%d, Revenue=%.2f", year, stats.totalTicketsSold, stats.totalRevenue));
                // TODO: Add this row to the placeholder table area
            } else {
                System.out.println("No annual data found for year: " + year);
                // TODO: Indicate missing data in the table or skip the year
            }
        }

        updateAnnualRevenueChart(yearsInRange); // Update chart based on the selected years

        // TODO: Update the placeholder table area with data from statsList
        // If using TableView, update its items here.
    }

    private void updateAnnualRevenueChart(List<Integer> years) {
        annualRevenueChart.getData().clear(); // Clear previous data

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu"); // Name for the series, used in legend if visible

        for (int year : years) {
            AnnualStats stats = mockAnnualData.get(year);
            if (stats != null) {
                series.getData().add(new XYChart.Data<>(String.valueOf(year), stats.totalRevenue));
            }
            // If stats is null, the year won't appear on the chart unless you add an empty data point
        }

        annualRevenueChart.getData().add(series);

        // Optional: Apply custom colors to bars if needed (blue in image)
        // This is easier with CSS using .chart-bar#data<index>.series<index>
        // Or you can lookup nodes:
        // annualRevenueChart.lookupAll(".chart-bar").forEach(node -> node.setStyle("-fx-bar-fill: #007bff;"));

        // Hide default legend if using custom legend in FXML
        annualRevenueChart.setLegendVisible(false);
    }

    private void clearMonthlyStatsTable() {
        System.out.println("Clearing monthly stats table placeholder.");
        // TODO: Implement clearing TableView or other placeholder content
    }

    private void clearAnnualRangeTable() {
        System.out.println("Clearing annual range table placeholder.");
        // TODO: Implement clearing TableView or other placeholder content
    }

    private void clearAnnualRevenueChart() {
        annualRevenueChart.getData().clear(); // Clear data
    }

    // --- Button Handler Placeholder ---
    private void handleCustomerTrend() {
        System.out.println("Button 'Thống kê xu hướng khách hàng' clicked.");
        // TODO: Implement logic to show customer trend statistics
        // This would likely involve loading another FXML or displaying a dialog.
    }
}