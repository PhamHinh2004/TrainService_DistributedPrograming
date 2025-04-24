package Controller;

// Import the new NhanVien model class (assuming KhachHang is in Model package)
import Model.KhachHang;
// Import the GioiTinh enum
import Enum.GioiTinh;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList; // For searching/filtering
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*; // Import all control classes
import javafx.scene.control.cell.PropertyValueFactory; // To link TableColumn to model properties
import javafx.scene.input.KeyCode; // For handling Enter key in search

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Arrays; // Needed for Arrays.asList
import java.util.stream.Collectors; // Needed for Collectors.toList

public class QuanLyKhachHangController implements Initializable {

    // --- Customer Information Section ---
    @FXML private TextField txtMaKhachHang;
    @FXML private TextField txtHoVaTen; // Maps to tenKhachHang in model
    @FXML private TextField txtCccd; // Maps to CCCD in model
    @FXML private TextField txtEmail;
    @FXML private TextField txtSoDienThoai;
    // ComboBox for GioiTinh, populated with String representations of the enum
    @FXML private ComboBox<String> cbGioiTinh;


    @FXML private Button btnCapNhat;
    @FXML private Button btnLamMoi;

    // --- Search Section ---
    @FXML private TextField txtTimKiem;

    // --- Table View ---
    // TableView type is now KhachHang
    @FXML private TableView<KhachHang> customerTable;
    @FXML private TableColumn<KhachHang, Integer> colStt;
    @FXML private TableColumn<KhachHang, String> colMaKhachHang;
    // TableColumn for HoVaTen, links to "tenKhachHang" property
    @FXML private TableColumn<KhachHang, String> colHoVaTen;
    @FXML private TableColumn<KhachHang, String> colSoDienThoai;
    @FXML private TableColumn<KhachHang, String> colEmail;
    // TableColumn for GioiTinh, links to "gioiTinh" property (which is Enum, but toString() is used by default)
    @FXML private TableColumn<KhachHang, GioiTinh> colGioiTinh; // Can use GioiTinh type if you want
    // Removed colDiaChi as it's not in the provided model


    // --- Data ---
    // List types are now KhachHang
    private ObservableList<KhachHang> masterCustomerList = FXCollections.observableArrayList();
    private FilteredList<KhachHang> filteredCustomerList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate Giới tính ComboBox with String representations of the enum
        cbGioiTinh.setItems(FXCollections.observableArrayList(
                Arrays.asList(GioiTinh.values()).stream()
                        .map(Enum::toString) // Convert enum values to String
                        .collect(Collectors.toList())
        ));

        // Set up TableView columns
        setupTableColumns();

        // Load mock data (replace with actual data loading from DB/API)
        loadMockCustomerData(); // This will now load KhachHang objects

        // Wrap the ObservableList in a FilteredList (for searching)
        filteredCustomerList = new FilteredList<>(masterCustomerList, p -> true); // Initially show all data

        // Set the filtered data to the TableView
        customerTable.setItems(filteredCustomerList);

        // Add listeners and button actions
        setupListeners();
        setupButtonActions();

        // Handle row selection in the table
        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showCustomerDetails(newSelection);
            } else {
                clearCustomerDetails(); // Clear fields if no row is selected
            }
        });

        // Initialize fields to be clear
        clearCustomerDetails();
    }

    // --- Table Setup ---
    private void setupTableColumns() {
        // Link TableColumn to KhachHang model properties using PropertyValueFactory
        // Use the exact getter names from the KhachHang model (without get/is/property)
        colMaKhachHang.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));
        colHoVaTen.setCellValueFactory(new PropertyValueFactory<>("tenKhachHang")); // Link to "tenKhachHang"
        colSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        // PropertyValueFactory for Enum automatically uses toString()
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        // colDiaChi was removed from FXML and model
        // colCccd is in info section but not in table in the image


        // Handle STT (Ordinal Number) - requires custom CellValueFactory
        colStt.setCellValueFactory(cellData -> {
            // Find the index of the current item in the *filtered* list
            // This is a bit hacky but works for STT
            int index = filteredCustomerList.getSourceIndex(cellData.getTableView().getItems().indexOf(cellData.getValue()));
            // Add 1 because index is 0-based
            return new javafx.beans.property.SimpleIntegerProperty(index + 1).asObject();
        });
        colStt.setSortable(false); // STT shouldn't be sortable this way
    }

    // --- Mock Data Loading ---
    private void loadMockCustomerData() {
        // Clear existing data
        masterCustomerList.clear();

        // Add some dummy customers using the KhachHang AllArgsConstructor
        // KhachHang(String maKhachHang, String tenKhachHang, String soDienThoai, String email, GioiTinh gioiTinh, String CCCD)
        masterCustomerList.addAll(
                new KhachHang("KH001", "Nguyễn Văn A", "0900000001", "a.nguyen@example.com", GioiTinh.NAM, "111111111111"),
                new KhachHang("KH002", "Trần Thị B", "0900000002", "b.tran@example.com", GioiTinh.NU, "222222222222"),
                new KhachHang("KH003", "Lê Văn C", "0900000003", "c.le@example.com", GioiTinh.NAM, "333333333333"),
                new KhachHang("KH001", "Nguyễn Văn A", "0900000001", "a.nguyen@example.com", GioiTinh.NAM, "111111111111"),
                new KhachHang("KH002", "Trần Thị B", "0900000002", "b.tran@example.com", GioiTinh.NU, "222222222222"),
                new KhachHang("KH003", "Lê Văn C", "0900000003", "c.le@example.com", GioiTinh.NAM, "333333333333"),
                new KhachHang("KH001", "Nguyễn Văn A", "0900000001", "a.nguyen@example.com", GioiTinh.NAM, "111111111111"),
                new KhachHang("KH002", "Trần Thị B", "0900000002", "b.tran@example.com", GioiTinh.NU, "222222222222"),
                new KhachHang("KH003", "Lê Văn C", "0900000003", "c.le@example.com", GioiTinh.NAM, "333333333333"),
                new KhachHang("KH001", "Nguyễn Văn A", "0900000001", "a.nguyen@example.com", GioiTinh.NAM, "111111111111"),
                new KhachHang("KH002", "Trần Thị B", "0900000002", "b.tran@example.com", GioiTinh.NU, "222222222222"),
                new KhachHang("KH003", "Lê Văn C", "0900000003", "c.le@example.com", GioiTinh.NAM, "333333333333")
                // Add more mock data as needed
        );

        System.out.println("Loaded " + masterCustomerList.size() + " mock customers.");
    }

    // --- UI Update Methods ---
    // Parameter type is KhachHang
    private void showCustomerDetails(KhachHang khachHang) {
        txtMaKhachHang.setText(khachHang.getMaKhachHang());
        txtHoVaTen.setText(khachHang.getTenKhachHang()); // Use getTenKhachHang()
        txtCccd.setText(khachHang.getCCCD()); // Use getCCCD() (uppercase)
        txtSoDienThoai.setText(khachHang.getSoDienThoai());
        txtEmail.setText(khachHang.getEmail());
        // Set ComboBox value using the String representation of the enum
        cbGioiTinh.setValue(khachHang.getGioiTinh() != null ? khachHang.getGioiTinh().toString() : null);

        // DiaChi is not in info fields based on the image/new model
    }

    private void clearCustomerDetails() {
        txtMaKhachHang.setText("");
        txtHoVaTen.setText("");
        txtCccd.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        cbGioiTinh.setValue(null); // Clear ComboBox selection
        customerTable.getSelectionModel().clearSelection(); // Deselect row
    }


    // --- Event Handlers Setup ---
    private void setupListeners() {
        // Listener for the search text field
        txtTimKiem.textProperty().addListener((obs, oldVal, newVal) -> {
            // Filter on KhachHang objects
            filteredCustomerList.setPredicate(khachHang -> {
                // If search text is empty, display all customers
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newVal.toLowerCase();

                // Check if search text matches Name or Phone number (based on prompt text)
                // Use new getter names and null checks
                if (khachHang.getTenKhachHang() != null && khachHang.getTenKhachHang().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Match on Name
                } else if (khachHang.getSoDienThoai() != null && khachHang.getSoDienThoai().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Match on Phone
                }
                // No match
                return false;
            });
        });

        // Optional: Trigger search on Enter key press in search field
        txtTimKiem.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // The listener above already handles filtering on text change,
                // but you could add specific action here if needed (e.g., highlight first result)
                System.out.println("Customer Search triggered by Enter: " + txtTimKiem.getText());
            }
        });
    }


    private void setupButtonActions() {
        // Handle "Làm mới" button action
        btnLamMoi.setOnAction(event -> clearCustomerDetails());

        // Handle "Cập nhật" button action
        btnCapNhat.setOnAction(event -> handleUpdateCustomer());
    }

    // --- Button Action Implementations (Placeholders) ---
    private void handleUpdateCustomer() {
        // Get selected KhachHang object
        KhachHang selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            // Get updated data from fields
            String newHoVaTen = txtHoVaTen.getText();
            String newCccd = txtCccd.getText();
            String newSoDienThoai = txtSoDienThoai.getText();
            String newEmail = txtEmail.getText();
            String selectedGioiTinhString = cbGioiTinh.getValue(); // Get String from ComboBox
            GioiTinh newGioiTinh = null;
            if (selectedGioiTinhString != null) {
                // Convert String from ComboBox back to Enum
                newGioiTinh = GioiTinh.valueOf(selectedGioiTinhString);
            }

            // Basic validation (add more robust validation as needed)
            if (newHoVaTen.isEmpty() || newCccd.isEmpty() || newSoDienThoai.isEmpty() || newEmail.isEmpty() || newGioiTinh == null) {
                showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng điền đầy đủ thông tin cập nhật.");
                return;
            }

            // Update the customer object using its setters
            selectedCustomer.setTenKhachHang(newHoVaTen); // Use setTenKhachHang()
            selectedCustomer.setCCCD(newCccd); // Use setCCCD()
            selectedCustomer.setSoDienThoai(newSoDienThoai);
            selectedCustomer.setEmail(newEmail);
            selectedCustomer.setGioiTinh(newGioiTinh); // Set GioiTinh enum

            // TODO: Save changes to your actual data source (database/API)
            // Example: customerService.update(selectedCustomer);

            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thông tin khách hàng đã được cập nhật.");
            // Refresh table display to show updated values
            customerTable.refresh();
        } else {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng chọn một khách hàng để cập nhật.");
        }
    }


    // Helper to show alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}