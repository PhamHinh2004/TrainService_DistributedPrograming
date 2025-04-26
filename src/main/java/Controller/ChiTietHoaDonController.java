package Controller;

import Model.HoaDon;
import Model.Ve;
import Model.Ghe;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ChiTietHoaDonController implements Initializable {

    @FXML
    private Label maHdLabel;

    @FXML
    private Label ngayLapLabel;

    @FXML
    private Label tenKhLabel;

    @FXML
    private Label tongTienLabel;

    @FXML
    private Label trangThaiLabel;

    @FXML
    private Label nhanVienLabel;

    @FXML
    private TableView<Ve> veTable;

    @FXML
    private TableColumn<Ve, Number> sttVeColumn;

    @FXML
    private TableColumn<Ve, String> maVeColumn;

    @FXML
    private TableColumn<Ve, String> loaiVeColumn;

    @FXML
    private TableColumn<Ve, String> maGheColumn;

    @FXML
    private TableColumn<Ve, String> gaDiColumn;

    @FXML
    private TableColumn<Ve, String> gaDenColumn;

    @FXML
    private TableColumn<Ve, Double> giaVeColumn;

    private HoaDon currentHoaDon;

    public void setHoaDon(HoaDon hoaDon) {
        this.currentHoaDon = hoaDon;
        if (hoaDon != null) {
            maHdLabel.setText(hoaDon.getMaHoaDon());
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Corrected format
            if (hoaDon.getNgayLapHoaDon() != null) {
                ngayLapLabel.setText(hoaDon.getNgayLapHoaDon().format(dateFormatter));
            } else {
                ngayLapLabel.setText("[Ngày lập không có]");
            }
            tenKhLabel.setText(hoaDon.getTenNguoiMua());
            tongTienLabel.setText(String.format("%,.0f VNĐ", hoaDon.getTongTien()));
            trangThaiLabel.setText(hoaDon.getTrangThai().toString());
            nhanVienLabel.setText(hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getTenNhanVien() : "Không có");
            veTable.setItems(FXCollections.observableArrayList(hoaDon.getDsVe()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sttVeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(veTable.getItems().indexOf(cellData.getValue()) + 1));
        maVeColumn.setCellValueFactory(new PropertyValueFactory<>("maVe"));
        loaiVeColumn.setCellValueFactory(new PropertyValueFactory<>("loaiVe"));
        maGheColumn.setCellValueFactory(cellData -> {
            Ghe ghe = cellData.getValue().getGhe();
            return new SimpleStringProperty(ghe != null ? ghe.getMaGhe() : "");
        });
        gaDiColumn.setCellValueFactory(cellData -> new SimpleStringProperty(currentHoaDon != null ? currentHoaDon.getGaKhoiHanh() : ""));
        gaDenColumn.setCellValueFactory(cellData -> new SimpleStringProperty(currentHoaDon != null ? currentHoaDon.getGaDen() : ""));
        giaVeColumn.setCellValueFactory(new PropertyValueFactory<>("giaVe"));
    }
}