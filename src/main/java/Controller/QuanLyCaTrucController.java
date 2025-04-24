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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller cho giao diện Quản Lý Ca Trực.
 */
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
    @FXML private TextField thatThoatTextField; // Đã thay thế hoTenTextField
    @FXML private DatePicker filterDatePicker;
    @FXML private Button filterButton;
    @FXML private Button showAllButton;
    @FXML private TableView<CaTrucRecord> caTrucTableView;
    @FXML private TableColumn<CaTrucRecord, Integer> sttCol;
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

    // --- khai báo lớp DAO ---
    private CaTrucDao caTrucDao = new CaTrucDao(CaTruc.class);
    private NhanVienDAO nhanVienDAO = new NhanVienDAO(NhanVien.class);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadMasterTableData();
        filteredCaTrucData = new FilteredList<>(masterCaTrucData, p -> true);
        setupDatePicker();
        setupTableView();
        setupTableRowSelectionListener();
        clearEmployeeInfoFields();
    }

    /** Cấu hình DatePicker lọc. */
    private void setupDatePicker() {
        filterDatePicker.setValue(LocalDate.now());
    }

    /** Cấu hình TableView. */
    private void setupTableView() {
        sttCol.setCellValueFactory(new PropertyValueFactory<>("stt"));
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
        thatThoatTextField.setText(record.getThatThoatFormatted()); // Hiển thị thất thoát
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

    /** Tải dữ liệu gốc (cần thay thế bằng logic thực tế). */
    private void loadMasterTableData() {
        masterCaTrucData.clear();

        masterCaTrucData.addAll(
                new CaTrucRecord(1, "NV001", LocalDate.of(2025, 4, 11), 56, 1040000, 14000000, 20, "Nguyễn Văn A", "a.nguyen@email.com", "0901112233", "123456789012", "Nam", LocalTime.of(8, 0), LocalTime.of(17, 0), 50000.0),
                new CaTrucRecord(2, "NV002", LocalDate.of(2025, 4, 11), 45, 850000, 11000000, 15, "Trần Thị B", "b.tran@email.com", "0904445566", "987654321098", "Nữ", LocalTime.of(13, 0), LocalTime.of(21, 0), 0.0),
                new CaTrucRecord(3, "NV004", LocalDate.of(2025, 4, 11), 62, 1250000, 16500000, 25, "Thế Khánh", "thekha@gmail.com", "0561561546", "0452737778", "Nam", LocalTime.of(9, 0), LocalTime.of(16, 0), 15000.0),
                new CaTrucRecord(4, "NV001", LocalDate.of(2025, 4, 12), 60, 1100000, 15000000, 22, "Nguyễn Văn A", "a.nguyen@email.com", "0901112233", "123456789012", "Nam", LocalTime.of(8, 0), LocalTime.of(17, 0), 20000.0),
                new CaTrucRecord(5, "NV003", LocalDate.of(2025, 4, 12), 55, 1000000, 13500000, 18, "Lê Văn C", "c.le@email.com", "0907778899", "555666777888", "Nam", LocalTime.of(7, 30), LocalTime.of(15, 30), 0.0)
        );
    }


    // --- Event Handlers ---
    @FXML
    void handleSearchAction(ActionEvent event) {
        String searchText = searchTextField.getText().trim();
        System.out.println("Đang tìm kiếm: " + searchText);
        // TODO: Logic tìm kiếm
        caTrucTableView.getSelectionModel().clearSelection();
        clearEmployeeInfoFields();
    }

    @FXML
    void handleFilterAction(ActionEvent event) {
        LocalDate selectedDate = filterDatePicker.getValue();
        System.out.println("Đang lọc dữ liệu cho ngày: " + (selectedDate != null ? selectedDate.format(dateFormatter) : "null"));
        caTrucTableView.getSelectionModel().clearSelection();
        clearEmployeeInfoFields();
        if (selectedDate == null) {
            filteredCaTrucData.setPredicate(null);
        } else {
            filteredCaTrucData.setPredicate(record -> record.getNgayCaTruc() != null && record.getNgayCaTruc().equals(selectedDate));
        }
    }

    @FXML
    void handleShowAllAction(ActionEvent event) {
        System.out.println("Hiển thị tất cả dữ liệu.");
        caTrucTableView.getSelectionModel().clearSelection();
        clearEmployeeInfoFields();
        filterDatePicker.setValue(null);
        filteredCaTrucData.setPredicate(null);
    }


    // --- Lớp Model Dữ liệu CaTrucRecord ---
    public static class CaTrucRecord {
        private final int stt;
        private final String maNhanVien;
        private final LocalDate ngayCaTruc;
        private final int soVeDaBan;
        private final double soTienTraLai;
        private final double doanhThu;
        private final int soHdLap;
        private final String hoTen; // Vẫn giữ để có thể dùng nếu cần
        private final String email; // Vẫn giữ
        private final String sdt; // Vẫn giữ
        private final String cccd; // Vẫn giữ
        private final String gioiTinh; // Vẫn giữ
        private final LocalTime gioVaoCa;
        private final LocalTime gioTanCa;
        private final double thatThoat; // Thuộc tính mới

        private static final Locale VIETNAM_LOCALE = new Locale("vi", "VN");
        private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(VIETNAM_LOCALE);
        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm a");

        public CaTrucRecord(int stt, String maNhanVien, LocalDate ngayCaTruc, int soVeDaBan,
                            double soTienTraLai, double doanhThu, int soHdLap,
                            String hoTen, String email, String sdt, String cccd, String gioiTinh,
                            LocalTime gioVaoCa, LocalTime gioTanCa, double thatThoat) {
            this.stt = stt;
            this.maNhanVien = maNhanVien;
            this.ngayCaTruc = ngayCaTruc;
            this.soVeDaBan = soVeDaBan;
            this.soTienTraLai = soTienTraLai;
            this.doanhThu = doanhThu;
            this.soHdLap = soHdLap;
            this.hoTen = hoTen;
            this.email = email;
            this.sdt = sdt;
            this.cccd = cccd;
            this.gioiTinh = gioiTinh;
            this.gioVaoCa = gioVaoCa;
            this.gioTanCa = gioTanCa;
            this.thatThoat = thatThoat;
        }

        // Getters
        public int getStt() { return stt; }
        public String getMaNhanVien() { return maNhanVien; }
        public LocalDate getNgayCaTruc() { return ngayCaTruc; }
        public int getSoVeDaBan() { return soVeDaBan; }
        public double getSoTienTraLai() { return soTienTraLai; }
        public double getDoanhThu() { return doanhThu; }
        public int getSoHdLap() { return soHdLap; }
        public String getHoTen() { return hoTen; }
        public String getEmail() { return email; }
        public String getSdt() { return sdt; }
        public String getCccd() { return cccd; }
        public String getGioiTinh() { return gioiTinh; }
        public LocalTime getGioVaoCa() { return gioVaoCa; }
        public LocalTime getGioTanCa() { return gioTanCa; }
        public double getThatThoat() { return thatThoat; }

        // Getters định dạng
        public String getNgayCaTrucFormatted() { return (ngayCaTruc != null) ? ngayCaTruc.format(DATE_FORMATTER) : ""; }
        public String getSoTienTraLaiFormatted() { return CURRENCY_FORMATTER.format(this.soTienTraLai).replace("₫", "VND"); }
        public String getDoanhThuFormatted() { return CURRENCY_FORMATTER.format(this.doanhThu).replace("₫", "VND"); }
        public String getGioVaoCaFormatted() { return (gioVaoCa != null) ? gioVaoCa.format(TIME_FORMATTER) : ""; }
        public String getGioTanCaFormatted() { return (gioTanCa != null) ? gioTanCa.format(TIME_FORMATTER) : ""; }
        public String getThatThoatFormatted() { return CURRENCY_FORMATTER.format(this.thatThoat).replace("₫", "VND"); }

    } // Kết thúc lớp CaTrucRecord

} // Kết thúc lớp QuanLyCaTrucController