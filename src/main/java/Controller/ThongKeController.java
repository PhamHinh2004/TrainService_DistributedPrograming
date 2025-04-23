package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.time.Year; // For getting current year

public class ThongKeController implements Initializable {

    @FXML
    private ComboBox<Integer> cbYear;

    @FXML
    private Label lblBestEmployeeId;
    @FXML
    private Label lblBestEmployeeName;
    @FXML
    private Label lblBestEmployeeGender;
    @FXML
    private Label lblBestEmployeeDOB;
    @FXML
    private Label lblBestEmployeePhone;
    @FXML
    private Label lblBestEmployeeRevenue;

    @FXML
    private BarChart<String, Number> revenueChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    // --- Mock Data Structures ---
    // Map to hold revenue data for each year
    private final Map<Integer, List<EmployeeRevenue>> annualRevenueData = new HashMap<>();
    // Map to hold best employee data for each year
    private final Map<Integer, EmployeeDetails> bestEmployeeData = new HashMap<>();

    // Simple class to represent employee revenue data point
    private static class EmployeeRevenue {
        String name;
        double revenue;

        EmployeeRevenue(String name, double revenue) {
            this.name = name;
            this.revenue = revenue;
        }
    }

    // Simple class to represent best employee details
    private static class EmployeeDetails {
        String id;
        String name;
        String gender;
        String dob; // Simplified as String
        String phone;
        double revenue;

        EmployeeDetails(String id, String name, String gender, String dob, String phone, double revenue) {
            this.id = id;
            this.name = name;
            this.gender = gender;
            this.dob = dob;
            this.phone = phone;
            this.revenue = revenue;
        }
    }
    // --- End Mock Data Structures ---


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // --- Populate Mock Data (replace with real data fetching) ---
        populateMockData();
        // --- End Populate Mock Data ---

        // Populate the year ComboBox
        ObservableList<Integer> years = FXCollections.observableArrayList(annualRevenueData.keySet());
        cbYear.setItems(years);

        // Set a listener for the year ComboBox
        cbYear.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadStatisticsForYear(newValue);
            }
        });

        // Select the current year or the latest year available by default
        int currentYear = Year.now().getValue();
        if (years.contains(currentYear)) {
            cbYear.getSelectionModel().select(Integer.valueOf(currentYear));
        } else if (!years.isEmpty()) {
            // Select the latest year in the data if current year isn't available
            years.sort(Integer::compareTo);
            cbYear.getSelectionModel().selectLast();
        } else {
            // Handle case where there is no data at all
            displayNoData();
        }

        // Configure the chart axes (can also be done in FXML)
        xAxis.setLabel("Nhân viên");
        yAxis.setLabel("Doanh thu (triệu VNĐ)");
       // yAxis.setTickLabelFormatter(new NumberAxis.Default NumberFormat()); // Default format
        // Optional: Adjust tick unit based on data range if needed
    }

    // --- Mock Data Population ---
    private void populateMockData() {
        // Data for 2022
        List<EmployeeRevenue> revenue2022 = new ArrayList<>();
        revenue2022.add(new EmployeeRevenue("An", 55.0));
        revenue2022.add(new EmployeeRevenue("Tân", 70.5)); // Higher revenue
        revenue2022.add(new EmployeeRevenue("Huy", 62.0));
        revenue2022.add(new EmployeeRevenue("Hoàng", 48.5));
        revenue2022.add(new EmployeeRevenue("Lân", 40.0));
        revenue2022.add(new EmployeeRevenue("Bình", 58.0));
        annualRevenueData.put(2022, revenue2022);
        // Best employee for 2022 (Tân)
        bestEmployeeData.put(2022, new EmployeeDetails("NV002", "Nguyễn Văn Tân", "Nam", "15/03/1990", "0987654321", 70.5));


        // Data for 2023
        List<EmployeeRevenue> revenue2023 = new ArrayList<>();
        revenue2023.add(new EmployeeRevenue("An", 60.0));
        revenue2023.add(new EmployeeRevenue("Tân", 65.0));
        revenue2023.add(new EmployeeRevenue("Huy", 55.0));
        revenue2023.add(new EmployeeRevenue("Hoàng", 75.0)); // Higher revenue
        revenue2023.add(new EmployeeRevenue("Lân", 45.0));
        revenue2023.add(new EmployeeRevenue("Bình", 68.0));
        annualRevenueData.put(2023, revenue2023);
        // Best employee for 2023 (Hoàng)
        bestEmployeeData.put(2023, new EmployeeDetails("NV004", "Trần Đình Hoàng", "Nam", "22/07/1995", "0123456789", 75.0));

        // Data for 2024 (Assume current year)
        List<EmployeeRevenue> revenue2024 = new ArrayList<>();
        revenue2024.add(new EmployeeRevenue("An", 70.0)); // Higher revenue
        revenue2024.add(new EmployeeRevenue("Tân", 68.0));
        revenue2024.add(new EmployeeRevenue("Huy", 65.0));
        revenue2024.add(new EmployeeRevenue("Hoàng", 72.0));
        revenue2024.add(new EmployeeRevenue("Lân", 50.0));
        revenue2024.add(new EmployeeRevenue("Bình", 60.0));
        annualRevenueData.put(2024, revenue2024);
        // Best employee for 2024 (An)
        bestEmployeeData.put(2024, new EmployeeDetails("NV001", "Lê Thị An", "Nữ", "01/01/1998", "0909090909", 70.0));
    }
    // --- End Mock Data Population ---


    // Method to load and display data for a selected year
    private void loadStatisticsForYear(int year) {
        System.out.println("Loading statistics for year: " + year); // Debug print

        // Load revenue data for chart
        List<EmployeeRevenue> revenueData = annualRevenueData.get(year);
        if (revenueData != null && !revenueData.isEmpty()) {
            updateRevenueChart(revenueData);
        } else {
            clearChart();
            System.out.println("No revenue data found for year: " + year);
        }

        // Load best employee data
        EmployeeDetails bestEmployee = bestEmployeeData.get(year);
        if (bestEmployee != null) {
            updateBestEmployeeDetails(bestEmployee);
        } else {
            clearBestEmployeeDetails();
            System.out.println("No best employee data found for year: " + year);
        }
    }

    // Method to update the BarChart
    private void updateRevenueChart(List<EmployeeRevenue> data) {
        revenueChart.getData().clear(); // Clear previous data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        // You could potentially name the series after the year: series.setName("Doanh thu " + selectedYear);

        for (EmployeeRevenue item : data) {
            series.getData().add(new XYChart.Data<>(item.name, item.revenue));
        }

        revenueChart.getData().add(series);

        // Optional: Apply specific styles to bars if needed
        // revenueChart.lookupAll(".chart-bar").forEach(node -> node.setStyle(...));
    }

    // Method to clear the BarChart
    private void clearChart() {
        revenueChart.getData().clear();
    }


    // Method to update the best employee labels
    private void updateBestEmployeeDetails(EmployeeDetails employee) {
        lblBestEmployeeId.setText(employee.id);
        lblBestEmployeeName.setText(employee.name);
        lblBestEmployeeGender.setText(employee.gender);
        lblBestEmployeeDOB.setText(employee.dob);
        lblBestEmployeePhone.setText(employee.phone);
        lblBestEmployeeRevenue.setText(String.format("%.2f triệu VNĐ", employee.revenue)); // Format revenue
    }

    // Method to clear the best employee labels
    private void clearBestEmployeeDetails() {
        lblBestEmployeeId.setText("---");
        lblBestEmployeeName.setText("---");
        lblBestEmployeeGender.setText("---");
        lblBestEmployeeDOB.setText("---");
        lblBestEmployeePhone.setText("---");
        lblBestEmployeeRevenue.setText("---");
    }

    // Method to display a message when no data is available at all
    private void displayNoData() {
        clearChart();
        clearBestEmployeeDetails();
        // You might add a label to the content area to say "No data available"
        System.out.println("No data available to display.");
    }
}