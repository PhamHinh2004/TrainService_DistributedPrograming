package Controller;

import Enum.TrangThaiHoaDon;
import Model.HoaDon;
import Dao.HoaDonDAO;
import Model.NhanVien;
import Model.Ve;
import Util.HoaDonExporter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class QuanLyHoaDonController implements Initializable {

    @FXML
    private TextField txtSearch;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnDetail;

    @FXML
    private Button btnExport;

    @FXML
    private TableView<HoaDon> invoiceTable;

    @FXML
    private TableColumn<HoaDon, Integer> sttColumn;

    @FXML
    private TableColumn<HoaDon, String> maHdColumn;

    @FXML
    private TableColumn<HoaDon, String> tenKhColumn;

    @FXML
    private TableColumn<HoaDon, Double> thanhTienColumn;

    @FXML
    private TableColumn<HoaDon, String> ngayLapColumn;

    @FXML
    private TableColumn<HoaDon, Integer> soLuongColumn;
    @FXML
    private TableColumn<HoaDon, String> loaiHoaDonColumn;

    @FXML
    private TableColumn<HoaDon, Double> tongTienColumn;

    @FXML
    private TableColumn<HoaDon, String> trangThaiColumn;

    @FXML
    private TableColumn<HoaDon, String> nhanVienColumn;

    private ObservableList<HoaDon> allInvoices;
    private ObservableList<HoaDon> filteredInvoices;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private HoaDonDAO hoaDonDAO;
    private HoaDonExporter hoaDonExporter;

    public QuanLyHoaDonController() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
        hoaDonDAO = new HoaDonDAO(entityManager);
        hoaDonExporter = new HoaDonExporter();
        filteredInvoices = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadData();
        setupButtonActions();
        setupComboBoxListener();
        setupSearchTextListener();
        loadStatusOptions();
        invoiceTable.setItems(filteredInvoices);
    }

    private void setupTableColumns() {
        sttColumn.setCellValueFactory((cellData) -> new SimpleIntegerProperty(invoiceTable.getItems().indexOf(cellData.getValue()) + 1).asObject());
        maHdColumn.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));
        loaiHoaDonColumn.setCellValueFactory(new PropertyValueFactory<>("loaiHoaDon"));
        tenKhColumn.setCellValueFactory(new PropertyValueFactory<>("tenNguoiMua"));
        thanhTienColumn.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        ngayLapColumn.setCellValueFactory(new PropertyValueFactory<>("ngayLapHoaDon"));
        soLuongColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDsVe() != null ? cellData.getValue().getDsVe().size() : 0).asObject());
        tongTienColumn.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        trangThaiColumn.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        nhanVienColumn.setCellValueFactory(cellData -> {
            NhanVien nhanVien = cellData.getValue().getNhanVien();
            return new SimpleStringProperty(nhanVien != null ? nhanVien.getTenNhanVien() : "Không có");
        });
    }

    public void loadData() {
        List<HoaDon> hoaDons = hoaDonDAO.getAllHoaDonWithDetails();
        if (hoaDons != null) {
            allInvoices = FXCollections.observableArrayList(hoaDons);
            filteredInvoices.setAll(allInvoices);
        } else {
            System.err.println("Lỗi khi tải dữ liệu hóa đơn.");
        }
    }

    private void setupButtonActions() {
        btnSearch.setOnAction(this::handleSearchAction);
        btnDetail.setOnAction(this::handleDetailAction);
        btnExport.setOnAction(this::handleExportAction);
    }

    @FXML
    void handleSearchAction(ActionEvent event) {
        String searchText = txtSearch.getText().toLowerCase();
        List<HoaDon> searchResult = allInvoices.stream()
                .filter(hoaDon -> hoaDon.getMaHoaDon().toLowerCase().contains(searchText) ||
                        hoaDon.getTenNguoiMua().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        if (!searchResult.isEmpty()) {
            filteredInvoices.setAll(searchResult);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Không tìm thấy hóa đơn phù hợp.");
            alert.showAndWait();
            filteredInvoices.setAll(allInvoices);
        }
    }

    @FXML
    void handleDetailAction(ActionEvent event) {
        HoaDon selectedInvoice = invoiceTable.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChiTietHoaDon.fxml"));
                Parent root = loader.load();

                ChiTietHoaDonController chiTietController = loader.getController();
                chiTietController.setHoaDon(selectedInvoice);

                Stage stage = new Stage();
                stage.setTitle("Chi tiết hóa đơn: " + selectedInvoice.getMaHoaDon());
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Không thể hiển thị chi tiết hóa đơn.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một hóa đơn để xem chi tiết.");
            alert.showAndWait();
        }
    }

    @FXML
    void handleExportAction(ActionEvent event) {
        HoaDon selectedInvoice = invoiceTable.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
            // Kiểm tra trạng thái hóa đơn trước khi in
            if (selectedInvoice.getTrangThai() == TrangThaiHoaDon.KHONGSUDUNG) {
                // Cập nhật trạng thái thành SUDUNG
                selectedInvoice.setTrangThai(TrangThaiHoaDon.SUDUNG);
                hoaDonDAO.updateHoaDon(selectedInvoice); // Giả sử bạn có phương thức updateHoaDon trong HoaDonDAO
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Trạng thái hóa đơn đã được cập nhật thành 'SUDUNG'.");
                alert.showAndWait();
            }
            hoaDonExporter.generateInvoicePDF(selectedInvoice);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một hóa đơn để xuất.");
            alert.showAndWait();
        }
    }

    private void setupComboBoxListener() {
        cmbStatus.valueProperty().addListener((obs, oldStatus, newStatus) -> {
            filterInvoices(txtSearch.getText().toLowerCase(), newStatus);
        });
    }

    private void setupSearchTextListener() {
        txtSearch.textProperty().addListener((obs, oldText, newText) -> {
            filterInvoices(newText.toLowerCase(), cmbStatus.getValue());
        });
    }

    private void loadStatusOptions() {
        try {
            ObservableList<String> statusNames = FXCollections.observableArrayList();
            statusNames.add(null);
            for (TrangThaiHoaDon status : TrangThaiHoaDon.values()) {
                statusNames.add(status.toString());
            }
            cmbStatus.setItems(statusNames);
            cmbStatus.setValue(null);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải trạng thái hóa đơn: " + e.getMessage());
        }
    }

    private void filterInvoices(String searchText, String selectedStatus) {
        filteredInvoices.clear();
        for (HoaDon hoaDon : allInvoices) {
            boolean matchesSearch = searchText.isEmpty() ||
                    hoaDon.getMaHoaDon().toLowerCase().contains(searchText) ||
                    hoaDon.getTenNguoiMua().toLowerCase().contains(searchText);
            boolean matchesStatus = (selectedStatus == null || hoaDon.getTrangThai().toString().equalsIgnoreCase(selectedStatus));
            if (matchesSearch && matchesStatus) {
                filteredInvoices.add(hoaDon);
            }
        }
        invoiceTable.setItems(filteredInvoices);
    }

    public void shutdown() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}