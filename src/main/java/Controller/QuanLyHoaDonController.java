package Controller;

import Model.HoaDon; // Import lớp HoaDon bạn vừa tạo

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class QuanLyHoaDonController implements Initializable {

    // Khai báo các control từ FXML (đảm bảo fx:id trùng khớp)
    @FXML
    private TextField txtSearch; // Tên fx:id cho TextField tìm kiếm

    @FXML
    private ComboBox<String> cmbStatus; // Tên fx:id cho ComboBox trạng thái

    @FXML
    private Button btnSearch; // Tên fx:id cho nút Tìm kiếm

    @FXML
    private Button btnDetail; // Tên fx:id cho nút Chi tiết

    @FXML
    private Button btnExport; // Tên fx:id cho nút Xuất File

    @FXML
    private TableView<HoaDon> invoiceTable; // Tên fx:id cho TableView, kiểu dữ liệu là HoaDon

    // Khai báo các cột trong TableView (đảm bảo fx:id trùng khớp và kiểu dữ liệu phù hợp)
    @FXML
    private TableColumn<HoaDon, Integer> sttColumn;

    @FXML
    private TableColumn<HoaDon, String> maHdColumn;

    @FXML
    private TableColumn<HoaDon, String> maKhColumn;

    @FXML
    private TableColumn<HoaDon, Double> thanhTienColumn; // Kiểu Double cho tiền

    @FXML
    private TableColumn<HoaDon, String> ngayLapColumn;

    @FXML
    private TableColumn<HoaDon, Integer> soLuongColumn; // Kiểu Integer cho số lượng

    @FXML
    private TableColumn<HoaDon, Double> tongTienColumn; // Kiểu Double cho tiền

    @FXML
    private TableColumn<HoaDon, String> trangThaiColumn;

    // ObservableList để chứa dữ liệu cho TableView
    private ObservableList<HoaDon> invoiceList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Thiết lập cách hiển thị dữ liệu cho các cột
        // PropertyValueFactory sử dụng tên của phương thức getter trong lớp HoaDon (không có "get")
        sttColumn.setCellValueFactory(new PropertyValueFactory<>("stt"));
        maHdColumn.setCellValueFactory(new PropertyValueFactory<>("maHd"));
        maKhColumn.setCellValueFactory(new PropertyValueFactory<>("maKh"));
        thanhTienColumn.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        ngayLapColumn.setCellValueFactory(new PropertyValueFactory<>("ngayLap"));
        soLuongColumn.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        tongTienColumn.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        trangThaiColumn.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        // 2. (Tạm thời) Tạo dữ liệu mẫu và thêm vào invoiceList
        invoiceList = FXCollections.observableArrayList(
//                new HoaDon(1, "HD001", "KH001", 100000.0, "2023-10-26", 2, 200000.0, "Đã in"),
//                new HoaDon(2, "HD002", "KH002", 150000.0, "2023-10-25", 1, 150000.0, "Chưa in"),
//                new HoaDon(3, "HD003", "KH001", 50000.0, "2023-10-26", 3, 150000.0, "Đã in")
                // Thêm các hóa đơn mẫu khác nếu muốn
        );

        // 3. Đặt dữ liệu mẫu vào TableView
        invoiceTable.setItems(invoiceList);

        // 4. Thiết lập các sự kiện cho các control (chỉ in console tạm thời)
        setupButtonActions();
        setupComboBoxListener();

        System.out.println("QuanLyHoaDonController initialized.");
    }

    // Phương thức thiết lập hành động cho các nút
    private void setupButtonActions() {
        btnSearch.setOnAction(this::handleSearchAction); // Sử dụng method reference
        btnDetail.setOnAction(this::handleDetailAction);
        btnExport.setOnAction(this::handleExportAction);
        // Các nút khác nếu có
    }

    // Phương thức xử lý sự kiện nhấn nút Tìm kiếm
    @FXML
    void handleSearchAction(ActionEvent event) {
        String searchText = txtSearch.getText();
        String selectedStatus = cmbStatus.getValue();
        System.out.println("Tìm kiếm với từ khóa: \"" + searchText + "\" và trạng thái: \"" + selectedStatus + "\"");
        // Logic tìm kiếm thực tế sẽ được thêm vào đây
    }

    // Phương thức xử lý sự kiện nhấn nút Chi tiết
    @FXML
    void handleDetailAction(ActionEvent event) {
        HoaDon selectedInvoice = invoiceTable.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
//            System.out.println("Xem chi tiết hóa đơn: " + selectedInvoice.getMaHd());
            // Logic hiển thị cửa sổ chi tiết sẽ được thêm vào đây
        } else {
            System.out.println("Chưa chọn hóa đơn nào để xem chi tiết.");
            // Tùy chọn: Hiển thị Alert thông báo
        }
    }

    // Phương thức xử lý sự kiện nhấn nút Xuất File
    @FXML
    void handleExportAction(ActionEvent event) {
        System.out.println("Xuất danh sách hóa đơn ra file.");
        // Logic xuất file sẽ được thêm vào đây
    }

    // Phương thức thiết lập lắng nghe sự kiện cho ComboBox
    private void setupComboBoxListener() {
        cmbStatus.valueProperty().addListener((obs, oldStatus, newStatus) -> {
            System.out.println("Trạng thái được chọn: " + newStatus);
            // Logic lọc dữ liệu theo trạng thái sẽ được thêm vào đây
        });
    }

    // Bạn có thể thêm các phương thức khác cần thiết cho màn hình Quản lý Hóa đơn
}
