package Controller;

// import Model.Employee; // Remove the old Employee import
import Model.NhanVien; // Import the new NhanVien model class // Keep this import only if you are still using the simple Employee class somewhere else, otherwise remove it. Assuming you only need NhanVien now.
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList; // For searching/filtering
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*; // Import all control classes
import javafx.scene.control.cell.PropertyValueFactory; // To link TableColumn to model properties
import javafx.scene.input.KeyCode; // For handling Enter key in search

import java.net.URL;
import java.time.LocalDate; // Needed for ngaySinh
import java.util.HashSet; // Needed for Set types in NhanVien constructor
import java.util.ResourceBundle;

// Import Enums used in NhanVien
import Enum.GioiTinh;
import Enum.TrangThaiNhanVien;


public class QuanLyNhanVienController implements Initializable {

    // --- Employee Information Section ---
    @FXML private TextField txtMaNhanVien;
    @FXML private TextField txtEmail;
    @FXML private TextField txtHoVaTen; // This maps to tenNhanVien in NhanVien
    @FXML private TextField txtSoDienThoai;
    @FXML private TextField txtViTri; // This maps to chucVu in NhanVien
    @FXML private ComboBox<GioiTinh> cbGioiTinh; // Use GioiTinh enum
    @FXML private TextField txtCccd; // This maps to CCCD (uppercase) in NhanVien

    @FXML private Button btnCapNhat;
    @FXML private Button btnLamMoi;

    // --- Search Section ---
    @FXML private TextField txtTimKiem;

    // --- New Account Section ---
    @FXML private TextField txtNewCccd; // Maps to CCCD
    @FXML private PasswordField pfNewPassword; // Related to TaiKhoan, not directly in NhanVien
    @FXML private PasswordField pfConfirmPassword; // Related to TaiKhoan
    @FXML private TextField txtNewOtp; // Related to TaiKhoan process
    @FXML private Button btnGetOtp;
    @FXML private TextField txtNewHoVaTen; // Maps to tenNhanVien
    @FXML private TextField txtNewEmail; // Maps to email
    @FXML private TextField txtNewSoDienThoai; // Maps to soDienThoai
    @FXML private ComboBox<GioiTinh> cbNewGioiTinh; // Use GioiTinh enum
    @FXML private Button btnTaoTaiKhoan; // Creates both NhanVien and related TaiKhoan


    // --- Table View ---
    // Change TableView and TableColumn types to NhanVien
    @FXML private TableView<NhanVien> employeeTable;
    @FXML private TableColumn<NhanVien, Integer> colStt; // STT (Ordinal Number)
    @FXML private TableColumn<NhanVien, String> colMaNhanVien;
    @FXML private TableColumn<NhanVien, String> colHoVaTen; // Maps to tenNhanVien
    @FXML private TableColumn<NhanVien, String> colCccd; // Maps to CCCD
    @FXML private TableColumn<NhanVien, String> colSoDienThoai;
    @FXML private TableColumn<NhanVien, String> colEmail;
    @FXML private TableColumn<NhanVien, GioiTinh> colGioiTinh; // Use GioiTinh enum
    @FXML private TableColumn<NhanVien, String> colViTri; // Maps to chucVu


    // --- Data ---
    // Change ObservableList and FilteredList types to NhanVien
    private ObservableList<NhanVien> masterEmployeeList = FXCollections.observableArrayList();
    private FilteredList<NhanVien> filteredEmployeeList;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate ComboBoxes with Enum values
        cbGioiTinh.setItems(FXCollections.observableArrayList(GioiTinh.values()));
        cbNewGioiTinh.setItems(FXCollections.observableArrayList(GioiTinh.values()));

        // Set up TableView columns
        setupTableColumns();

        // Load mock data (replace with actual data loading from DB/API)
        loadMockEmployeeData(); // Note: This method will now create NhanVien objects

        // Wrap the ObservableList in a FilteredList (for searching)
        filteredEmployeeList = new FilteredList<>(masterEmployeeList, p -> true); // Initially show all data

        // Set the filtered data to the TableView
        employeeTable.setItems(filteredEmployeeList);

        // Add listeners and button actions
        setupListeners();
        setupButtonActions();

        // Handle row selection in the table
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showEmployeeDetails(newSelection);
            } else {
                clearEmployeeDetails(); // Clear fields if no row is selected
            }
        });

        // Initialize fields to be clear
        clearEmployeeDetails();
        clearNewAccountForm();
    }

    // --- Table Setup ---
    private void setupTableColumns() {
        // Link TableColumn to NhanVien model properties using PropertyValueFactory
        // Use property names from the NhanVien class (tenNhanVien, CCCD, chucVu)
        colMaNhanVien.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        colHoVaTen.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien")); // Use "tenNhanVien"
        colCccd.setCellValueFactory(new PropertyValueFactory<>("CCCD")); // Use "CCCD" (uppercase)
        colSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colViTri.setCellValueFactory(new PropertyValueFactory<>("chucVu")); // Use "chucVu"

        // Handle STT (Ordinal Number) - requires custom CellValueFactory or binding
        colStt.setCellValueFactory(cellData -> {
            // Find the index of the current item in the *filtered* list
            int index = filteredEmployeeList.getSourceIndex(cellData.getTableView().getItems().indexOf(cellData.getValue()));
            // Add 1 because index is 0-based
            return new javafx.beans.property.SimpleIntegerProperty(index + 1).asObject();
        });
        colStt.setSortable(false); // STT shouldn't be sortable in this way
    }

    // --- Mock Data Loading ---
    private void loadMockEmployeeData() {
        // Clear existing data
        masterEmployeeList.clear();

        // Add some dummy NhanVien using the AllArgsConstructor
        // NhanVien(String maNhanVien, String tenNhanVien, String soDienThoai, String email, GioiTinh gioiTinh, String CCCD, LocalDate ngaySinh, String chucVu, TrangThaiNhanVien trangThai, Set<CaTruc> catruc, TaiKhoan taikhoan, Set<HoaDon> hoaDons)
        masterEmployeeList.addAll(
                new NhanVien("NV001", "Lê Thị An", "0909090909", "an.le@example.com", GioiTinh.NU, "123456789012", LocalDate.of(1998, 1, 1), "Nhân viên bán hàng", TrangThaiNhanVien.DANGLAM, new HashSet<>(), null, new HashSet<>()),
                new NhanVien("NV002", "Nguyễn Văn Tân", "0987654321", "tan.nguyen@example.com", GioiTinh.NAM, "234567890123", LocalDate.of(1990, 3, 15), "Quản lý", TrangThaiNhanVien.DANGLAM, new HashSet<>(), null, new HashSet<>()),
                new NhanVien("NV003", "Trần Thị Huy", "0123456789", "huy.tran@example.com", GioiTinh.NU, "345678901234", LocalDate.of(1995, 7, 22), "Nhân viên kỹ thuật", TrangThaiNhanVien.DANGLAM, new HashSet<>(), null, new HashSet<>()),
                new NhanVien("NV004", "Phạm Văn Hoàng", "0369871234", "hoang.pham@example.com", GioiTinh.NAM, "456789012345", LocalDate.of(1992, 11, 30), "Nhân viên bán hàng", TrangThaiNhanVien.DANGLAM, new HashSet<>(), null, new HashSet<>()),
                new NhanVien("NV005", "Đặng Thị Lân", "0912345678", "lan.dang@example.com", GioiTinh.NU, "567890123456", LocalDate.of(1997, 5, 10), "Kế toán", TrangThaiNhanVien.DANGLAM, new HashSet<>(), null, new HashSet<>()),
                new NhanVien("NV006", "Bùi Văn Bình", "0977777777", "binh.bui@example.com", GioiTinh.NAM, "678901234567", LocalDate.of(1988, 9, 5), "Trưởng phòng", TrangThaiNhanVien.DANGLAM, new HashSet<>(), null, new HashSet<>())
        );

        System.out.println("Loaded " + masterEmployeeList.size() + " mock employees.");
    }

    // --- UI Update Methods ---
    // Change parameter type to NhanVien
    private void showEmployeeDetails(NhanVien employee) {
        txtMaNhanVien.setText(employee.getMaNhanVien());
        txtHoVaTen.setText(employee.getTenNhanVien()); // Use getTenNhanVien()
        txtCccd.setText(employee.getCCCD()); // Use getCCCD() (uppercase)
        txtSoDienThoai.setText(employee.getSoDienThoai());
        txtEmail.setText(employee.getEmail());
        cbGioiTinh.setValue(employee.getGioiTinh()); // Value is GioiTinh enum
        txtViTri.setText(employee.getChucVu()); // Use getChucVu()
    }

    private void clearEmployeeDetails() {
        txtMaNhanVien.setText("");
        txtHoVaTen.setText("");
        txtCccd.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        cbGioiTinh.setValue(null);
        txtViTri.setText("");
        employeeTable.getSelectionModel().clearSelection(); // Deselect row
    }

    private void clearNewAccountForm() {
        txtNewCccd.setText("");
        pfNewPassword.setText("");
        pfConfirmPassword.setText("");
        txtNewOtp.setText("");
        txtNewHoVaTen.setText("");
        txtNewEmail.setText("");
        txtNewSoDienThoai.setText("");
        cbNewGioiTinh.setValue(null);
    }

    // --- Event Handlers Setup ---
    private void setupListeners() {
        // Listener for the search text field
        txtTimKiem.textProperty().addListener((obs, oldVal, newVal) -> {
            // Change parameter type to NhanVien
            filteredEmployeeList.setPredicate(nhanVien -> {
                // If search text is empty, display all employees
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newVal.toLowerCase();

                // Check if search text matches employee ID or phone number
                // Use NhanVien methods
                if (nhanVien.getMaNhanVien() != null && nhanVien.getMaNhanVien().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Match on ID
                } else if (nhanVien.getSoDienThoai() != null && nhanVien.getSoDienThoai().toLowerCase().contains(lowerCaseFilter)) {
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
                System.out.println("Search triggered by Enter: " + txtTimKiem.getText());
            }
        });
    }


    private void setupButtonActions() {
        // Handle "Làm mới" button action
        btnLamMoi.setOnAction(event -> clearEmployeeDetails());

        // Handle "Cập nhật" button action
        btnCapNhat.setOnAction(event -> handleUpdateEmployee());

        // Handle "Tạo tài khoản" button action
        btnTaoTaiKhoan.setOnAction(event -> handleCreateNewAccount());

        // Handle "Lấy mã OTP" button action (placeholder)
        btnGetOtp.setOnAction(event -> handleGetOtp());
    }

    // --- Button Action Implementations (Placeholders) ---
    private void handleUpdateEmployee() {
        // Change selected type to NhanVien
        NhanVien selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            // Get updated data from fields
            String newHoVaTen = txtHoVaTen.getText();
            String newCccd = txtCccd.getText();
            String newSoDienThoai = txtSoDienThoai.getText();
            String newEmail = txtEmail.getText();
            GioiTinh newGioiTinh = cbGioiTinh.getValue(); // Value is GioiTinh enum
            String newViTri = txtViTri.getText();

            // Basic validation (add more robust validation as needed)
            if (newHoVaTen.isEmpty() || newCccd.isEmpty() || newSoDienThoai.isEmpty() || newEmail.isEmpty() || newGioiTinh == null || newViTri.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng điền đầy đủ thông tin cập nhật.");
                return;
            }

            // Update the NhanVien object using its setters
            selectedEmployee.setTenNhanVien(newHoVaTen); // Use setTenNhanVien()
            selectedEmployee.setCCCD(newCccd); // Use setCCCD()
            selectedEmployee.setSoDienThoai(newSoDienThoai);
            selectedEmployee.setEmail(newEmail);
            selectedEmployee.setGioiTinh(newGioiTinh);
            selectedEmployee.setChucVu(newViTri); // Use setChucVu()

            // TODO: Save changes to your actual data source (database/API)
            // This is where you would call a service method like employeeService.update(selectedEmployee);

            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thông tin nhân viên đã được cập nhật.");
            // Refresh table display if necessary (ObservableList might not detect nested changes without refresh)
            employeeTable.refresh();
        } else {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng chọn một nhân viên để cập nhật.");
        }
    }

    private void handleCreateNewAccount() {
        // Get data from new account fields
        String cccd = txtNewCccd.getText(); // Maps to CCCD
        String password = pfNewPassword.getText(); // Related to TaiKhoan
        String confirmPassword = pfConfirmPassword.getText(); // Related to TaiKhoan
        String otp = txtNewOtp.getText(); // Related to TaiKhoan process
        String hoVaTen = txtNewHoVaTen.getText(); // Maps to tenNhanVien
        String email = txtNewEmail.getText(); // Maps to email
        String soDienThoai = txtNewSoDienThoai.getText(); // Maps to soDienThoai
        GioiTinh gioiTinh = cbNewGioiTinh.getValue(); // Value is GioiTinh enum
        String chucVu = "Nhân viên mới"; // Default position for new account

        // Basic validation
        if (cccd.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || hoVaTen.isEmpty() || email.isEmpty() || soDienThoai.isEmpty() || gioiTinh == null) {
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng điền đầy đủ thông tin tạo tài khoản.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Mật khẩu và xác nhận mật khẩu không khớp.");
            return;
        }
        // TODO: Add more validation (email format, phone format, CCCD format, password strength, OTP validation)

        // TODO: Implement actual account creation logic (call to backend/service)
        // This would involve:
        // 1. Creating both a NhanVien and a TaiKhoan object.
        // 2. Linking them.
        // 3. Sending data to your server/service.
        // 4. Server handles user creation, hashing password, verifying OTP, etc.
        // 5. Server returns a result (success/failure, new employee ID).

        // --- Mock Creation (for UI demo) ---
        // In a real app, the server would generate the ID and handle most of this.
        String newEmployeeId = "NV" + (masterEmployeeList.size() + 1); // Simple mock ID generation

        // Create a new NhanVien object using the AllArgsConstructor
        // Provide dummy/default values for fields not in the form
        NhanVien newEmployee = new NhanVien(
                newEmployeeId,           // maNhanVien
                hoVaTen,                 // tenNhanVien (from form)
                soDienThoai,             // soDienThoai (from form)
                email,                   // email (from form)
                gioiTinh,                // gioiTinh (from form)
                cccd,                    // CCCD (from form)
                LocalDate.now(),         // ngaySinh (Dummy value, should get from form or be optional)
                chucVu,                  // chucVu (Default value)
                TrangThaiNhanVien.DANGLAM, // trangThai (Default value)
                new HashSet<>(),         // catruc (Empty set)
                null,                    // taikhoan (Will be created and linked by service)
                new HashSet<>()          // hoaDons (Empty set)
        );

        // Add to the master list (FilteredList updates automatically)
        masterEmployeeList.add(newEmployee);
        // TODO: You should also save this new employee to your database/source
        // Example: employeeService.create(newEmployee, password, otp); // Pass password and OTP separately

        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tài khoản nhân viên mới đã được tạo.");
        clearNewAccountForm(); // Clear the form after successful creation

        // Optional: Select the newly created employee in the table
        employeeTable.getSelectionModel().select(newEmployee);
        employeeTable.scrollTo(newEmployee);
        // --- End Mock Creation ---
    }

    private void handleGetOtp() {
        System.out.println("Button 'Lấy mã OTP' clicked.");
        // TODO: Implement OTP request logic
        // Usually requires a phone number or email address to be entered first
        // Send request to backend -> backend sends OTP -> user enters OTP
        showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Chức năng 'Lấy mã OTP' chưa được triển khai.");
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