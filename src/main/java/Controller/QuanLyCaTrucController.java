package Controller;

import Dao.CaTrucDao;
// import Dao.NhanVienDAO; // Commented out if NhanVienDAO isn't directly used here
import Model.CaTruc;
// import Model.NhanVien; // Commented out if NhanVien isn't directly used here
import javafx.beans.property.ReadOnlyObjectWrapper; // **** ADDED Import ****
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
// import java.time.LocalDateTime; // Commented out if LocalDateTime isn't directly used here
import java.time.format.DateTimeFormatter;
// import java.util.ArrayList; // Commented out if ArrayList isn't directly used here
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class QuanLyCaTrucController implements Initializable {

    // --- FXML Components ---
    @FXML private VBox rootContainer;
    @FXML private Button searchButton;
    @FXML private TextField searchTextField;
    @FXML private TextField maNvTextField;
    @FXML private TextField ngayCaTrucTextField;
    @FXML private TextField gioVaoCaTextField;
    @FXML private TextField gioTanCaTextField;
    @FXML private TextField soVeDaBanDetailTextField;
    @FXML private TextField soTienTraLaiDetailTextField;
    @FXML private TextField doanhThuDetailTextField;
    @FXML private TextField soHdLapDetailTextField;
    @FXML private TextField thatThoatTextField;
    @FXML private DatePicker filterDatePicker;
    @FXML private TableView<CaTrucRecord> caTrucTableView;

    // **** ADDED FXML Injection for STT column ****
    @FXML private TableColumn<CaTrucRecord, String> sttCol;

    @FXML private TableColumn<CaTrucRecord, String> maNvCol;
    @FXML private TableColumn<CaTrucRecord, String> ngayCaTrucCol;
    @FXML private TableColumn<CaTrucRecord, Integer> soVeDaBanCol;
    @FXML private TableColumn<CaTrucRecord, String> soTienTraLaiCol;
    @FXML private TableColumn<CaTrucRecord, String> doanhThuCol;
    @FXML private TableColumn<CaTrucRecord, Integer> soHdLapCol;

    // --- Data ---
    private ObservableList<CaTrucRecord> masterCaTrucData = FXCollections.observableArrayList();
    private FilteredList<CaTrucRecord> filteredCaTrucData;

    // --- Formatters ---
    private final Locale vietnamLocale = new Locale("vi", "VN");
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnamLocale);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");

    // --- DAO ---
    private CaTrucDao caTrucDao = new CaTrucDao(CaTruc.class);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadMasterTableData();
        filteredCaTrucData = new FilteredList<>(masterCaTrucData, p -> true);
        setupTableView(); // This now includes STT column setup
        setupTableRowSelectionListener();
        clearEmployeeInfoFields();
    }

    /** Cấu hình TableView, bao gồm cả cột STT. */
    private void setupTableView() {
        // **** ADDED Setup for STT Column ****
        sttCol.setCellValueFactory(cellData -> {
            // Get the TableView instance from the cell data
            TableView<CaTrucRecord> tableView = cellData.getTableView();
            // Get the index of the current row within the *displayed* items
            int index = tableView.getItems().indexOf(cellData.getValue());
            // Add 1 to start numbering from 1 instead of 0
            return new ReadOnlyObjectWrapper<>(String.valueOf(index + 1)).asString();
        });
        sttCol.setStyle("-fx-alignment: CENTER;"); // Center align text in STT column

        // Existing column setups
        maNvCol.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        ngayCaTrucCol.setCellValueFactory(new PropertyValueFactory<>("ngayCaTrucFormatted"));
        soVeDaBanCol.setCellValueFactory(new PropertyValueFactory<>("soVeDaBan"));
        soTienTraLaiCol.setCellValueFactory(new PropertyValueFactory<>("soTienTraLaiFormatted"));
        doanhThuCol.setCellValueFactory(new PropertyValueFactory<>("doanhThuFormatted"));
        soHdLapCol.setCellValueFactory(new PropertyValueFactory<>("soHdLap"));

        // Align specific columns if needed (adjust as per your preference)
        soVeDaBanCol.setStyle("-fx-alignment: CENTER;");
        soHdLapCol.setStyle("-fx-alignment: CENTER;");
        soTienTraLaiCol.setStyle("-fx-alignment: CENTER-RIGHT;"); // Example: Right-align currency
        doanhThuCol.setStyle("-fx-alignment: CENTER-RIGHT;");    // Example: Right-align currency

        caTrucTableView.setItems(filteredCaTrucData);
    }

    /** Thiết lập trình lắng nghe sự kiện chọn dòng. */
    private void setupTableRowSelectionListener() {
        caTrucTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayEmployeeAndShiftInfo(newSelection);
            } else {
                clearEmployeeInfoFields();
            }
        });
    }

    /** Hiển thị thông tin chi tiết lên các trường. */
    private void displayEmployeeAndShiftInfo(CaTrucRecord record) {
        if (record == null) return;
        maNvTextField.setText(record.getMaNhanVien());
        ngayCaTrucTextField.setText(record.getNgayCaTrucFormatted());
        gioVaoCaTextField.setText(record.getGioVaoCaFormatted());
        gioTanCaTextField.setText(record.getGioTanCaFormatted());
        soVeDaBanDetailTextField.setText(String.valueOf(record.getSoVeDaBan()));
        soTienTraLaiDetailTextField.setText(record.getSoTienTraLaiFormatted());
        doanhThuDetailTextField.setText(record.getDoanhThuFormatted());
        soHdLapDetailTextField.setText(String.valueOf(record.getSoHdLap()));
        thatThoatTextField.setText(record.getThatThoatFormatted());
    }

    /** Xóa trống các trường thông tin. */
    private void clearEmployeeInfoFields() {
        maNvTextField.clear();
        ngayCaTrucTextField.clear();
        gioVaoCaTextField.clear();
        gioTanCaTextField.clear();
        soVeDaBanDetailTextField.clear();
        soTienTraLaiDetailTextField.clear();
        doanhThuDetailTextField.clear();
        soHdLapDetailTextField.clear();
        thatThoatTextField.clear();
    }

    /** Tải dữ liệu gốc từ DAO. */
    private void loadMasterTableData() {
        masterCaTrucData.clear();
        try {
            List<Model.CaTruc> caTrucList = caTrucDao.getAll();
            if (caTrucList != null && !caTrucList.isEmpty()) {
                for (Model.CaTruc caTruc : caTrucList) {
                    // Basic null check for nested NhanVien object inside CaTruc
                    if (caTruc != null && caTruc.getNhanVien() != null) {
                        masterCaTrucData.add(new CaTrucRecord(caTruc));
                    } else {
                        System.err.println("Warning: Skipping CaTruc record due to null CaTruc or NhanVien.");
                        // Handle this case more robustly if necessary, e.g., log details
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Keep logging the exception
            showAlert(Alert.AlertType.ERROR, "Lỗi Dữ Liệu", "Không thể tải danh sách ca trực. Lỗi: " + e.getMessage());
        }
    }


    // --- Event Handlers ---

    /**
     * Handles search action triggered by either the search button or pressing Enter
     * in the search text field.
     */
    @FXML
    void handleSearchAction(ActionEvent event) {
        applyFilters();
        searchTextField.clear();
    }

    @FXML
    void handleFilterAction(ActionEvent event) {
        applyFilters();
    }

    @FXML
    void handleShowAllAction(ActionEvent event) {
        filterDatePicker.setValue(null);
        searchTextField.clear(); // Clear search text when showing all
        applyFilters(); // Apply filters to reset the view
    }

    /**
     * Áp dụng bộ lọc kết hợp (ngày và văn bản tìm kiếm) cho FilteredList.
     */
    private void applyFilters() {
        LocalDate selectedDate = filterDatePicker.getValue();
        final String lowerCaseSearchText = searchTextField.getText().trim().toLowerCase();

        Predicate<CaTrucRecord> datePredicate = record -> {
            if (selectedDate == null) return true;
            LocalDate recordDate = record.getNgayCaTruc();
            return recordDate != null && recordDate.equals(selectedDate);
        };

        Predicate<CaTrucRecord> textPredicate = record -> {
            if (lowerCaseSearchText.isEmpty()) return true;

            String maNv = record.getMaNhanVien();
            String soVeStr = String.valueOf(record.getSoVeDaBan());
            String soHdStr = String.valueOf(record.getSoHdLap());

            // Added null checks for safety
            boolean maNvMatch = maNv != null && maNv.toLowerCase().contains(lowerCaseSearchText);
            boolean soVeMatch = soVeStr.contains(lowerCaseSearchText); // Assuming soVeStr is never null
            boolean soHdMatch = soHdStr.contains(lowerCaseSearchText); // Assuming soHdStr is never null

            return maNvMatch || soVeMatch || soHdMatch;
        };

        // Xóa lựa chọn cũ và thông tin chi tiết BEFORE applying the filter
        caTrucTableView.getSelectionModel().clearSelection();
        clearEmployeeInfoFields();

        // Áp dụng bộ lọc KẾT HỢP (AND giữa ngày và text)
        filteredCaTrucData.setPredicate(datePredicate.and(textPredicate));

        // Refresh the TableView to ensure STT numbers are updated correctly after filtering
        caTrucTableView.refresh();
    }


    /** Hiển thị thông báo Alert. */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // --- Lớp Model Dữ liệu CaTrucRecord (ViewModel - Giữ nguyên) ---
    public static class CaTrucRecord {
        private final Model.CaTruc caTruc;

        private static final Locale VIETNAM_LOCALE = new Locale("vi", "VN");
        private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(VIETNAM_LOCALE);
        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm a");

        public CaTrucRecord(Model.CaTruc caTruc) {
            if (caTruc == null) throw new IllegalArgumentException("CaTruc object cannot be null");
            // Added null check for NhanVien within CaTruc
            if (caTruc.getNhanVien() == null) throw new IllegalArgumentException("CaTruc must have a non-null NhanVien");
            this.caTruc = caTruc;
        }

        // Getters - Added safety check for NhanVien just in case, though constructor should prevent it
        public String getMaNhanVien() {
            return (caTruc.getNhanVien() != null && caTruc.getNhanVien().getMaNhanVien() != null)
                    ? caTruc.getNhanVien().getMaNhanVien() : "N/A";
        }
        public LocalDate getNgayCaTruc() { return (caTruc.getNgayGioBatDau() != null) ? caTruc.getNgayGioBatDau().toLocalDate() : null; }
        public LocalTime getGioVaoCa() { return (caTruc.getNgayGioBatDau() != null) ? caTruc.getNgayGioBatDau().toLocalTime() : null; }
        public LocalTime getGioTanCa() { return (caTruc.getNgayGioKetThuc() != null) ? caTruc.getNgayGioKetThuc().toLocalTime() : null; }
        public int getSoVeDaBan() { return caTruc.getTongSoVeBan(); }
        public double getSoTienTraLai() { return caTruc.getTongSoTienTraLai(); }
        public double getDoanhThu() { return caTruc.getTongKetThucThu(); }
        public int getSoHdLap() { return caTruc.getTongHoaDon(); }
        public double getThatThoat() { return caTruc.getThatThoat(); }
        public Model.CaTruc getOriginalCaTruc() { return caTruc; }

        // Getters định dạng
        public String getNgayCaTrucFormatted() { LocalDate date = getNgayCaTruc(); return (date != null) ? date.format(DATE_FORMATTER) : ""; }
        public String getSoTienTraLaiFormatted() { return CURRENCY_FORMATTER.format(this.getSoTienTraLai()).replace("₫", "VND"); }
        public String getDoanhThuFormatted() { return CURRENCY_FORMATTER.format(this.getDoanhThu()).replace("₫", "VND"); }
        public String getGioVaoCaFormatted() { LocalTime time = getGioVaoCa(); return (time != null) ? time.format(TIME_FORMATTER) : ""; }
        public String getGioTanCaFormatted() { LocalTime time = getGioTanCa(); return (time != null) ? time.format(TIME_FORMATTER) : ""; }
        public String getThatThoatFormatted() { return CURRENCY_FORMATTER.format(this.getThatThoat()).replace("₫", "VND"); }
    }
}