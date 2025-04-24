package Controller;

import Dao.CaTrucDao;
import Dao.NhanVienDAO;
import Model.CaTruc;
import Model.NhanVien;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        setupTableView();
        setupTableRowSelectionListener();
        clearEmployeeInfoFields();
    }

    /** Cấu hình TableView. */
    private void setupTableView() {
        maNvCol.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        ngayCaTrucCol.setCellValueFactory(new PropertyValueFactory<>("ngayCaTrucFormatted"));
        soVeDaBanCol.setCellValueFactory(new PropertyValueFactory<>("soVeDaBan"));
        soTienTraLaiCol.setCellValueFactory(new PropertyValueFactory<>("soTienTraLaiFormatted"));
        doanhThuCol.setCellValueFactory(new PropertyValueFactory<>("doanhThuFormatted"));
        soHdLapCol.setCellValueFactory(new PropertyValueFactory<>("soHdLap"));
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
            if (caTrucList != null && !caTrucList.isEmpty()) { // Kiểm tra !isEmpty quan trọng
                for (Model.CaTruc caTruc : caTrucList) {
                    masterCaTrucData.add(new CaTrucRecord(caTruc));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Dữ Liệu", "Không thể tải danh sách ca trực.");
        }
    }

    // --- Event Handlers ---
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
        applyFilters();
        searchTextField.clear();
    }

    /**
     * Áp dụng bộ lọc kết hợp (ngày và văn bản tìm kiếm) cho FilteredList.
     */
    private void applyFilters() {
        LocalDate selectedDate = filterDatePicker.getValue();
        final String lowerCaseSearchText = searchTextField.getText().trim().toLowerCase();

        // Tạo điều kiện lọc ngày
        Predicate<CaTrucRecord> datePredicate = record -> {
            if (selectedDate == null) return true; // Luôn đúng nếu không chọn ngày
            LocalDate recordDate = record.getNgayCaTruc();
            return recordDate != null && recordDate.equals(selectedDate);
        };

        // Tạo điều kiện lọc văn bản (tìm kiếm trong Mã NV, Số vé, Số HĐ)
        Predicate<CaTrucRecord> textPredicate = record -> {
            if (lowerCaseSearchText.isEmpty()) return true; // Luôn đúng nếu không nhập text

            String maNv = record.getMaNhanVien();
            String soVeStr = String.valueOf(record.getSoVeDaBan());
            String soHdStr = String.valueOf(record.getSoHdLap());

            boolean maNvMatch = maNv != null && maNv.toLowerCase().contains(lowerCaseSearchText);
            boolean soVeMatch = soVeStr.contains(lowerCaseSearchText);
            boolean soHdMatch = soHdStr.contains(lowerCaseSearchText);

            return maNvMatch || soVeMatch || soHdMatch; // Chỉ tìm Mã NV, Số vé, Số HĐ
        };

        // Xóa lựa chọn cũ và thông tin chi tiết
        caTrucTableView.getSelectionModel().clearSelection();
        clearEmployeeInfoFields();

        // Áp dụng bộ lọc KẾT HỢP (AND giữa ngày và text)
        filteredCaTrucData.setPredicate(datePredicate.and(textPredicate));
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
            this.caTruc = caTruc;
        }

        // Getters
        public String getMaNhanVien() { return (caTruc.getNhanVien().getMaNhanVien() != null) ? caTruc.getNhanVien().getMaNhanVien() : "N/A"; }
        public LocalDate getNgayCaTruc() { return (caTruc.getNgayGioBatDau() != null) ? caTruc.getNgayGioBatDau().toLocalDate() : null; }
        public LocalTime getGioVaoCa() { return (caTruc.getNgayGioBatDau() != null) ? caTruc.getNgayGioBatDau().toLocalTime() : null; }
        public LocalTime getGioTanCa() { return (caTruc.getNgayGioKetThuc() != null) ? caTruc.getNgayGioKetThuc().toLocalTime() : null; }
        public int getSoVeDaBan() { return caTruc.getTongSoVeBan(); }
        public double getSoTienTraLai() { return 0.0; }
        public double getDoanhThu() { return caTruc.getTongKetThucThu(); }
        public int getSoHdLap() { return caTruc.getTongHoaDon(); }
        public double getThatThoat() { return caTruc.getThatThoat(); }
        public Model.CaTruc getOriginalCaTruc() { return caTruc; }

        // Getters định dạng
        public String getNgayCaTrucFormatted() { LocalDate date = getNgayCaTruc(); return (date != null) ? date.format(DATE_FORMATTER) : ""; }
        public String getSoTienTraLaiFormatted() { return CURRENCY_FORMATTER.format(this.getSoTienTraLai()).replace("₫", "VND"); /* TODO: Cần logic lấy số tiền trả lại */ }
        public String getDoanhThuFormatted() { return CURRENCY_FORMATTER.format(this.getDoanhThu()).replace("₫", "VND"); }
        public String getGioVaoCaFormatted() { LocalTime time = getGioVaoCa(); return (time != null) ? time.format(TIME_FORMATTER) : ""; }
        public String getGioTanCaFormatted() { LocalTime time = getGioTanCa(); return (time != null) ? time.format(TIME_FORMATTER) : ""; }
        public String getThatThoatFormatted() { return CURRENCY_FORMATTER.format(this.getThatThoat()).replace("₫", "VND"); }
    }

}