package Controller;

import Model.Ve; // Import Ve model
import Model.KhachHang; // Import KhachHang model (nested)
import Enum.LoaiVe; // Import LoaiVe enum
import Enum.GioiTinh; // Import GioiTinh enum
import Enum.TrangThaiVe; // Import TrangThaiVe enum

import javafx.beans.property.SimpleObjectProperty; // Needed for derived properties returning objects
import javafx.beans.property.SimpleStringProperty; // Needed for derived String properties
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList; // For filtering
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*; // Import all control classes
import javafx.scene.control.cell.PropertyValueFactory; // To link TableColumn to model properties
import javafx.scene.input.KeyCode; // For handling Enter key

import java.net.URL;
import java.time.LocalDateTime; // For date/time model fields
import java.time.format.DateTimeFormatter; // For formatting dates/times
import java.util.ResourceBundle;


// Assuming you have these mock classes if needed for deriving data
// private static class MockGa { String tenGa; MockGa(String t) {tenGa=t;} public String getTenGa() {return tenGa;}}
// private static class MockChuyenTau { LocalDateTime ngayKhoiHanh; MockChuyenTau(LocalDateTime d) {ngayKhoiHanh=d;} public LocalDateTime getNgayKhoiHanh() {return ngayKhoiHanh;}}
// private static class MockGheVe { MockGa gaKhoiHanh; MockGa gaKetThuc; MockChuyenTau chuyenTau; MockGheVe(MockGa gh, MockGa gk, MockChuyenTau ct) {gaKhoiHanh=gh; gaKetThuc=gk; chuyenTau=ct;}}


public class QuanLyVeController implements Initializable {

    // --- Customer/Ticket Information Section (Display only) ---
    @FXML private TextField txtMaVe;
    @FXML private TextField txtMaKhachHang;
    @FXML private TextField txtHoVaTen;
    @FXML private TextField txtCccd;
    @FXML private TextField txtSoDienThoai;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> cbGioiTinh; // Displaying String

    // --- Search and Action Buttons ---
    @FXML private Button btnTimKiem;
    @FXML private TextField txtTimKiem; // Search field
    @FXML private Button btnChiTiet;
    @FXML private Button btnInVe;
    @FXML private Button btnHuyVe;
    @FXML private Button btnCapNhat; // Update button


    // --- Table View ---
    @FXML private TableView<Ve> ticketTable; // TableView type is Ve
    @FXML private TableColumn<Ve, Integer> colStt; // STT
    @FXML private TableColumn<Ve, String> colMaVe; // maVe
    @FXML private TableColumn<Ve, String> colDiemDi; // Derived/Placeholder
    @FXML private TableColumn<Ve, String> colDiemDen; // Derived/Placeholder
    @FXML private TableColumn<Ve, LoaiVe> colDoiTuong; // loaiVe (Enum)
    @FXML private TableColumn<Ve, Double> colGiaVe; // giaVe
    @FXML private TableColumn<Ve, LocalDateTime> colNgayDat; // ngayDat (needs formatting)
    @FXML private TableColumn<Ve, LocalDateTime> colNgayKhoiHanh; // Derived/Placeholder (needs formatting)


    // --- Data ---
    private ObservableList<Ve> masterTicketList = FXCollections.observableArrayList();
    private FilteredList<Ve> filteredTicketList;


    // --- Date/Time Formatters ---
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"); // Example combined format


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate Giới tính ComboBox with String values
        cbGioiTinh.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));

        // Set up TableView columns
        setupTableColumns();

        // Load mock data (replace with actual data loading from DB/API)
        loadMockTicketData();

        // Wrap the ObservableList in a FilteredList (for filtering)
        filteredTicketList = new FilteredList<>(masterTicketList, p -> true); // Initially show all data

        // Set the filtered data to the TableView
        ticketTable.setItems(filteredTicketList);

        // Add listeners and button actions
        setupListeners();
        setupButtonActions();

        // Handle row selection in the table
        ticketTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showTicketDetails(newSelection);
            } else {
                clearTicketDetails(); // Clear fields if no row is selected
            }
        });

        // Initialize fields to be clear
        clearTicketDetails();
    }

    // --- Table Setup ---
    private void setupTableColumns() {
        // Link TableColumn to Ve model properties or nested/derived values

        colMaVe.setCellValueFactory(new PropertyValueFactory<>("maVe")); // Direct property
        colDoiTuong.setCellValueFactory(new PropertyValueFactory<>("loaiVe")); // Direct property (Enum, toString() used)
        colGiaVe.setCellValueFactory(new PropertyValueFactory<>("giaVe")); // Direct property


        // Handle STT (Ordinal Number) - requires custom CellValueFactory
        colStt.setCellValueFactory(cellData -> {
            // Find the index of the current item in the *filtered* list
            int index = filteredTicketList.getSourceIndex(cellData.getTableView().getItems().indexOf(cellData.getValue()));
            // Add 1 because index is 0-based
            return new javafx.beans.property.SimpleIntegerProperty(index + 1).asObject();
        });
        colStt.setSortable(false); // STT shouldn't be sortable this way

        // Handle "Điểm đi" and "Điểm đến" (Derived from sttGaKhoiHnah and sttGaDen)
        // Use SimpleStringProperty to wrap the derived value for the TableView
        colDiemDi.setCellValueFactory(cellData -> {
            Ve ve = cellData.getValue();
            // Derive the station name from sttGaKhoiHnah
            String stationName = "Ga " + ve.getSttGaKhoiHnah();
            // In a real app, you might map sttGaKhoiHnah to a NhaGa object and get its name
            // String stationName = ve.getGhe() != null && ve.getGhe().getGaKhoiHanh() != null ? ve.getGhe().getGaKhoiHanh().getTenGa() : "N/A";
            return new SimpleStringProperty(stationName); // Wrap String in SimpleStringProperty
        });

        colDiemDen.setCellValueFactory(cellData -> {
            Ve ve = cellData.getValue();
            // Derive the station name from sttGaDen
            String stationName = "Ga " + ve.getSttGaDen();
            // In a real app: ve.getGhe() != null && ve.getGhe().getGaKetThuc() != null ? ve.getGhe().getGaKetThuc().getTenGa() : "N/A";
            return new SimpleStringProperty(stationName); // Wrap String in SimpleStringProperty
        });


        // Handle "Ngày đặt" (ngayDat) - needs formatting
        colNgayDat.setCellValueFactory(new PropertyValueFactory<>("ngayDat"));
        colNgayDat.setCellFactory(column -> new TableCell<Ve, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Format the date/time
                    setText(item.format(DATETIME_FORMAT));
                }
            }
        });
        colNgayDat.setComparator( (d1, d2) -> {
            if (d1 == null && d2 == null) return 0;
            if (d1 == null) return -1;
            if (d2 == null) return 1;
            return d1.compareTo(d2);
        }); // Comparator handles nulls


        // Handle "Ngày khởi hành" (Derived/Placeholder) - needs formatting
        // Assume this comes from a related entity, e.g., ve.getGhe().getChuyenTau().getNgayKhoiHanh()
        // For mock data, let's just use ngayDat + a few days as a placeholder
        colNgayKhoiHanh.setCellValueFactory(cellData -> {
            Ve ve = cellData.getValue();
            LocalDateTime ngayKhoiHanh = null;
            if (ve.getNgayDat() != null) {
                // Mocking ngayKhoiHanh = ngayDat + some random days (for demo)
                ngayKhoiHanh = ve.getNgayDat().plusDays( (long)(Math.random() * 5) + 1).withHour(8).withMinute(0); // Example time
                // In a real app: ve.getGhe() != null && ve.getGhe().getChuyenTau() != null ? ve.getGhe().getChuyenTau().getNgayKhoiHanh() : null;
            }
            return new SimpleObjectProperty<>(ngayKhoiHanh); // Wrap LocalDateTime in SimpleObjectProperty
        });
        colNgayKhoiHanh.setCellFactory(column -> new TableCell<Ve, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Format the date/time
                    setText(item.format(DATETIME_FORMAT));
                }
            }
        });
        colNgayKhoiHanh.setComparator( (d1, d2) -> {
            if (d1 == null && d2 == null) return 0;
            if (d1 == null) return -1;
            if (d2 == null) return 1;
            return d1.compareTo(d2);
        }); // Comparator handles nulls
    }

    // --- Mock Data Loading ---
    private void loadMockTicketData() {
        // Clear existing data
        masterTicketList.clear();

        // Create some mock KhachHang to link tickets to
        // Using the 6-argument constructor you provided in KhachHang.java
        KhachHang kh1 = new KhachHang("KH001", "Nguyễn Văn A", "0900000001", "a.nguyen@example.com", GioiTinh.NAM, "111111111111");
        KhachHang kh2 = new KhachHang("KH002", "Trần Thị B", "0900000002", "b.tran@example.com", GioiTinh.NU, "222222222222");
        KhachHang kh3 = new KhachHang("KH003", "Lê Văn C", "0900000003", "c.le@example.com", GioiTinh.NAM, "333333333333");


        // Add some dummy Ve objects
        // Ve(String maVe, LoaiVe loaiVe, double giaVe, int sttGaKhoiHnah, int sttGaDen, LocalDateTime ngayDat, LocalDateTime ngayChinhSuaGanNhat, TrangThaiVe trangThaiVe, HoaDon hoaDon, KhachHang khachHang, Ghe ghe)
        masterTicketList.addAll(
                new Ve("V001", LoaiVe.NGUOILON, 500000.0, 1, 5, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), TrangThaiVe.DAMUA, null, kh1, null), // From Ga 1 to Ga 5
                new Ve("V002", LoaiVe.TREEM, 250000.0, 1, 3, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1), TrangThaiVe.DAMUA, null, kh1, null), // From Ga 1 to Ga 3
                new Ve("V003", LoaiVe.NGUOILON, 700000.0, 2, 7, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3), TrangThaiVe.DAMUA, null, kh2, null), // From Ga 2 to Ga 7
                new Ve("V004", LoaiVe.NGUOILON, 500000.0, 3, 6, LocalDateTime.now().minusHours(12), LocalDateTime.now().minusHours(12), TrangThaiVe.DAMUA, null, kh2, null), // From Ga 3 to Ga 6
                new Ve("V005", LoaiVe.NGUOILON, 350000.0, 4, 8, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(5), TrangThaiVe.DAMUA, null, kh3, null) // From Ga 4 to Ga 8
                // Add more mock data as needed
        );

        System.out.println("Loaded " + masterTicketList.size() + " mock tickets.");
    }

    // --- UI Update Methods ---
    // Parameter type is Ve
    private void showTicketDetails(Ve ve) {
        // Show Ticket Info
        txtMaVe.setText(ve.getMaVe() != null ? ve.getMaVe() : "N/A");

        // Show Customer Details (from nested KhachHang)
        KhachHang khachHang = ve.getKhachHang();
        if (khachHang != null) {
            txtMaKhachHang.setText(khachHang.getMaKhachHang() != null ? khachHang.getMaKhachHang() : "N/A");
            txtHoVaTen.setText(khachHang.getTenKhachHang() != null ? khachHang.getTenKhachHang() : "N/A"); // Use tenKhachHang
            txtCccd.setText(khachHang.getCCCD() != null ? khachHang.getCCCD() : "N/A"); // Use CCCD (uppercase)
            txtSoDienThoai.setText(khachHang.getSoDienThoai() != null ? khachHang.getSoDienThoai() : "N/A");
            txtEmail.setText(khachHang.getEmail() != null ? khachHang.getEmail() : "N/A");
            cbGioiTinh.setValue(khachHang.getGioiTinh() != null ? khachHang.getGioiTinh().toString() : "N/A"); // Convert enum to String
        } else {
            // Clear customer fields if KhachHang is null
            txtMaKhachHang.setText("");
            txtHoVaTen.setText("");
            txtCccd.setText("");
            txtSoDienThoai.setText("");
            txtEmail.setText("");
            cbGioiTinh.setValue(null);
        }
    }

    private void clearTicketDetails() {
        // Clear Ticket Info
        txtMaVe.setText("");

        // Clear Customer Fields
        txtMaKhachHang.setText("");
        txtHoVaTen.setText("");
        txtCccd.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        cbGioiTinh.setValue(null);

        ticketTable.getSelectionModel().clearSelection(); // Deselect row
    }


    // --- Event Handlers Setup ---
    private void setupListeners() {
        // Listener for the search text field
        txtTimKiem.textProperty().addListener((obs, oldVal, newVal) -> applySearchFilter());

        // Optional: Trigger search on Enter key press in search field
        txtTimKiem.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                applySearchFilter(); // Apply filter when Enter is pressed
                System.out.println("Search triggered by Enter: " + txtTimKiem.getText());
            }
        });
    }


    private void setupButtonActions() {
        // Handle "Tìm kiếm" button action
        btnTimKiem.setOnAction(event -> applySearchFilter());

        // Handle "Chi tiết" button action (Placeholder)
        btnChiTiet.setOnAction(event -> handleChiTiet());

        // Handle "In Vé" button action (Placeholder)
        btnInVe.setOnAction(event -> handleInVe());

        // Handle "Hủy vé" button action (Placeholder)
        btnHuyVe.setOnAction(event -> handleHuyVe());

        // Handle "Cập nhật" button action (Placeholder - seems to update customer info)
        btnCapNhat.setOnAction(event -> handleCapNhatKhachHang());
    }

    // --- Filtering Logic ---
    private void applySearchFilter() {
        String searchText = txtTimKiem.getText();

        filteredTicketList.setPredicate(ve -> {
            // If search text is empty, display all tickets
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = searchText.toLowerCase();

            // Check if search text matches Ticket ID or Customer Name (based on prompt text)
            // Access nested KhachHang property carefully, handling nulls
            boolean idMatch = ve.getMaVe() != null && ve.getMaVe().toLowerCase().contains(lowerCaseFilter);

            boolean customerNameMatch = false;
            if (ve.getKhachHang() != null && ve.getKhachHang().getTenKhachHang() != null) {
                customerNameMatch = ve.getKhachHang().getTenKhachHang().toLowerCase().contains(lowerCaseFilter);
            }

            return idMatch || customerNameMatch; // Match if either ID or Customer Name matches
        });

        // Update STT after filtering (hacky way to force re-rendering)
        ticketTable.getColumns().get(0).setVisible(false);
        ticketTable.getColumns().get(0).setVisible(true);

        // Clear details and selection after filtering, as selection might be lost
        clearTicketDetails();
    }

    // --- Button Action Implementations (Placeholders) ---

    private void handleChiTiet() {
        Ve selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            System.out.println("Button 'Chi tiết' clicked for ticket: " + selectedTicket.getMaVe());
            // TODO: Implement logic to show detailed ticket information (e.g., in a new window/dialog)
            showAlert(Alert.AlertType.INFORMATION, "Chi tiết vé", "Chức năng 'Chi tiết' chưa được triển khai.\nVé đã chọn: " + selectedTicket.getMaVe());
        } else {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng chọn một vé để xem chi tiết.");
        }
    }

    private void handleInVe() {
        Ve selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            System.out.println("Button 'In Vé' clicked for ticket: " + selectedTicket.getMaVe());
            // TODO: Implement logic to generate and display/print the ticket
            showAlert(Alert.AlertType.INFORMATION, "In vé", "Chức năng 'In Vé' chưa được triển khai.\nVé đã chọn: " + selectedTicket.getMaVe());
        } else {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng chọn một vé để in.");
        }
    }

    private void handleHuyVe() {
        Ve selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            // Confirm cancellation
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Xác nhận hủy vé");
            confirmation.setHeaderText("Hủy vé " + selectedTicket.getMaVe() + "?");
            confirmation.setContentText("Bạn có chắc chắn muốn hủy vé này?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.out.println("Button 'Hủy vé' confirmed for ticket: " + selectedTicket.getMaVe());
                    // TODO: Implement logic to cancel the ticket in your data source
                    // Update the ticket status in the model (if TrangThaiVe is in the table, it would update)
                    // selectedTicket.setTrangThaiVe(TrangThaiVe.DaHuy); // Example
                    // masterTicketList.remove(selectedTicket); // Or remove from list if cancelled tickets are not shown

                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Vé " + selectedTicket.getMaVe() + " đã được hủy (chức năng chưa hoàn chỉnh).");
                    // Refresh table if status is not automatically updated
                    ticketTable.refresh();
                    clearTicketDetails(); // Clear details after cancelling
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng chọn một vé để hủy.");
        }
    }

    private void handleCapNhatKhachHang() {
        // This button seems to update the customer info associated with the selected ticket
        Ve selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null && selectedTicket.getKhachHang() != null) {
            KhachHang khachHangToUpdate = selectedTicket.getKhachHang();

            // The info fields are currently read-only based on the image.
            // If these fields were editable, you would get updated values like this:
            // String newHoVaTen = txtHoVaTen.getText();
            // String newCccd = txtCccd.getText();
            // ... etc.
            // And then set them on khachHangToUpdate:
            // khachHangToUpdate.setTenKhachHang(newHoVaTen);
            // khachHangToUpdate.setCCCD(newCccd);
            // ... set other properties ...

            // TODO: Save changes to your actual data source (database/API) for this KhachHang object
            // customerService.update(khachHangToUpdate);

            // For this read-only example, just show an alert
            showAlert(Alert.AlertType.INFORMATION, "Cập nhật khách hàng", "Chức năng 'Cập nhật khách hàng' chưa được triển khai.\n(Thông tin khách hàng đang ở chế độ hiển thị)");

            // If the fields become editable and you update, refresh the table if customer name/info is shown in table columns
            // ticketTable.refresh();
        } else if (selectedTicket != null && selectedTicket.getKhachHang() == null) {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Vé đã chọn không có thông tin khách hàng.");
        }
        else {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng chọn một vé để cập nhật thông tin khách hàng.");
        }
    }


    // Helper to show alerts (can reuse from previous controllers)
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}