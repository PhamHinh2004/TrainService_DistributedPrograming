package Controller;

// DAO Imports - Adjust path as necessary
import Dao.VeDAO;
import Dao.KhachHangDAO;
import Dao.NhaGaDAO;

// Model Imports - Adjust path as necessary
import Model.Ve;
import Model.KhachHang;
import Model.NhaGa;
import Model.Ghe;

// Enum Imports - Adjust path as necessary
import Enum.LoaiVe;
import Enum.TrangThaiVe;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class QuanLyVeController implements Initializable {

    // ... (Khai báo DAO và FXML giữ nguyên) ...
    private VeDAO veDAO = new VeDAO(Ve.class);
    private KhachHangDAO khachHangDAO = new KhachHangDAO(KhachHang.class);
    private NhaGaDAO nhaGaDAO = new NhaGaDAO(NhaGa.class);

    @FXML private TextField txtHoVaTenKH;
    @FXML private TextField txtSdtKH;
    @FXML private TextField txtCccdKH;
    @FXML private TextField txtEmailKH;
    @FXML private TextField txtLoaiVe;
    @FXML private TextField txtViTriGhe;
    @FXML private TextField txtGaKhoiHanh;
    @FXML private TextField txtNgayKhoiHanhDisplay;
    @FXML private TextField txtGiaVe;
    @FXML private TextField txtToa;
    @FXML private TextField txtGaDen;
    @FXML private TextField txtNgayDatDisplay;
    @FXML private DatePicker dpDateField;
    @FXML private Button btnTimKiem;
    @FXML private TextField txtTimKiem;
    @FXML private Button btnCapNhat;
    @FXML private Button btnInVe;
    @FXML private Button btnHuyVe;
    @FXML private TableView<Ve> ticketTable;
    @FXML private TableColumn<Ve, Integer> colStt;
    @FXML private TableColumn<Ve, String> colHoTenKH;
    @FXML private TableColumn<Ve, String> colGaKhoiHanh;
    @FXML private TableColumn<Ve, String> colGaDen;
    @FXML private TableColumn<Ve, LoaiVe> colLoaiVe;
    @FXML private TableColumn<Ve, Double> colGiaVe;
    @FXML private TableColumn<Ve, String> colGhe;
    @FXML private TableColumn<Ve, String> colToa;

    private ObservableList<Ve> masterTicketList = FXCollections.observableArrayList();
    private FilteredList<Ve> filteredTicketList;

    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadDataIntoTable();

        filteredTicketList = new FilteredList<>(masterTicketList, p -> true);
        ticketTable.setItems(filteredTicketList);

        setupListeners(); // <- Gọi setupListeners đã được sửa đổi
        setupButtonActions(); // <- Gọi setupButtonActions đã được sửa đổi

        clearTicketDetails();
        if (dpDateField != null) {
            dpDateField.setValue(LocalDate.now());
        }
        // <<< SỬA ĐỔI: Gọi hàm lọc theo ngày ban đầu >>>
        applyDateFilter();
        // <<< KẾT THÚC SỬA ĐỔI >>>
    }

    // --- Table Column Setup (Giữ nguyên code của bạn) ---
    private void setupTableColumns() {
        // Your existing implementation...
        colStt.setCellValueFactory(cellData -> new SimpleIntegerProperty(ticketTable.getItems().indexOf(cellData.getValue()) + 1).asObject());
        colHoTenKH.setCellValueFactory(cellData -> {
            Ve ve = cellData.getValue();
            String customerName = "";
            if (ve != null && ve.getKhachHang() != null && ve.getKhachHang().getTenKhachHang() != null) {
                customerName = ve.getKhachHang().getTenKhachHang();
            }
            return new SimpleStringProperty(customerName);
        });
        colGaKhoiHanh.setCellValueFactory(cellData -> { Ve ve = cellData.getValue(); String stationName = ""; if (ve != null) { try { NhaGa ga = nhaGaDAO.findBySTTNhaGa(ve.getSttGaKhoiHnah()); stationName = (ga != null && ga.getTenNhaGa() != null) ? ga.getTenNhaGa() : "Ga " + ve.getSttGaKhoiHnah(); } catch (Exception e) { stationName = "Ga " + ve.getSttGaKhoiHnah(); } } return new SimpleStringProperty(stationName); });
        colGaDen.setCellValueFactory(cellData -> { Ve ve = cellData.getValue(); String stationName = ""; if (ve != null) { try { NhaGa ga = nhaGaDAO.findBySTTNhaGa(ve.getSttGaDen()); stationName = (ga != null && ga.getTenNhaGa() != null) ? ga.getTenNhaGa() : "Ga " + ve.getSttGaDen(); } catch (Exception e) { stationName = "Ga " + ve.getSttGaDen(); } } return new SimpleStringProperty(stationName); });
        colLoaiVe.setCellValueFactory(new PropertyValueFactory<>("loaiVe"));
        colGiaVe.setCellValueFactory(new PropertyValueFactory<>("giaVe"));
        colGiaVe.setCellFactory(tc -> new TableCell<>() { @Override protected void updateItem(Double price, boolean empty) { super.updateItem(price, empty); setText(empty || price == null ? "" : CURRENCY_FORMAT.format(price)); } });
        colGhe.setCellValueFactory(cellData -> { Ve ve = cellData.getValue(); String seat = ""; if (ve != null && ve.getGhe() != null && ve.getGhe().getMaGhe() != null) { try { String maGhe = ve.getGhe().getMaGhe();  seat = maGhe; } catch (Exception e){ seat="?"; } } return new SimpleStringProperty(seat); });
        colToa.setCellValueFactory(cellData -> { Ve ve = cellData.getValue(); String coach = ""; if (ve != null && ve.getGhe().getToa().getTenToa() != null) { try { String tenToa = ve.getGhe().getToa().getTenToa(); coach = tenToa; } catch (Exception e){ coach="?"; } } return new SimpleStringProperty(coach); });
    }


    // --- Helper for Button Columns (Giữ nguyên code của bạn) ---
    private Callback<TableColumn<Ve, Void>, TableCell<Ve, Void>> createButtonCellFactory(String buttonText, String style) {
        // Your existing implementation...
        return param -> { final TableCell<Ve, Void> cell = new TableCell<>() { private final Button btn = new Button(buttonText); { btn.setStyle(style); btn.setOnAction(event -> { Ve ve = getTableView().getItems().get(getIndex()); handleXemChiTiet(ve); }); btn.setMaxWidth(Double.MAX_VALUE); setAlignment(Pos.CENTER); } @Override public void updateItem(Void item, boolean empty) { super.updateItem(item, empty); setGraphic(empty ? null : btn); } }; return cell; };
    }

    // --- Data Loading (Giữ nguyên code của bạn) ---
    private void loadDataIntoTable() {
        // Your existing implementation...
        try { List<Ve> veList = veDAO.getAll(); if (veList != null) { masterTicketList.setAll(veList); System.out.println("Loaded " + veList.size() + " tickets from DAO."); } else { System.err.println("VeDAO.getAll() returned null."); masterTicketList.clear(); showAlert(Alert.AlertType.ERROR, "Lỗi Tải Dữ Liệu", "Không thể tải danh sách vé (DAO trả về null)."); } } catch (Exception e) { System.err.println("Error loading data from VeDAO: " + e.getMessage()); e.printStackTrace(); masterTicketList.clear(); showAlert(Alert.AlertType.ERROR, "Lỗi Tải Dữ Liệu", "Đã xảy ra lỗi khi tải danh sách vé:\n" + e.getMessage()); }
    }

    // --- UI Update Methods (Giữ nguyên code của bạn) ---
    private void showTicketDetails(Ve ve) {
        // Your existing implementation...
        if (ve == null) { clearTicketDetails(); return; } KhachHang kh = null; if (ve.getKhachHang() != null && ve.getKhachHang().getMaKhachHang() != null) { try { kh = khachHangDAO.findById(ve.getKhachHang().getMaKhachHang()); } catch (Exception e) { System.err.println("Error fetching customer details for maKH " + ve.getKhachHang().getMaKhachHang() + ": " + e.getMessage()); } }
        txtHoVaTenKH.setText(kh != null && kh.getTenKhachHang() != null ? kh.getTenKhachHang() : ""); txtSdtKH.setText(kh != null && kh.getSoDienThoai() != null ? kh.getSoDienThoai() : ""); txtCccdKH.setText(kh != null && kh.getCCCD() != null ? kh.getCCCD() : ""); txtEmailKH.setText(kh != null && kh.getEmail() != null ? kh.getEmail() : "");
        txtLoaiVe.setText(ve.getLoaiVe() != null ? ve.getLoaiVe().toString() : ""); txtGiaVe.setText(ve.getGiaVe() != 0 ? CURRENCY_FORMAT.format(ve.getGiaVe()) : ""); txtNgayDatDisplay.setText(ve.getNgayDat() != null ? ve.getNgayDat().format(DATETIME_FORMAT) : "");
        String gaDiName = "Ga " + ve.getSttGaKhoiHnah(); try { NhaGa gaDi = nhaGaDAO.findBySTTNhaGa(ve.getSttGaKhoiHnah()); if (gaDi != null && gaDi.getTenNhaGa() != null) gaDiName = gaDi.getTenNhaGa(); } catch (Exception e) {} txtGaKhoiHanh.setText(gaDiName);
        String gaDenName = "Ga " + ve.getSttGaDen(); try { NhaGa gaDen = nhaGaDAO.findBySTTNhaGa(ve.getSttGaDen()); if (gaDen != null && gaDen.getTenNhaGa() != null) gaDenName = gaDen.getTenNhaGa(); } catch (Exception e) {} txtGaDen.setText(gaDenName);
        String seatInfo = ""; String coachInfo = ""; LocalDateTime departureTime = null;
        try { Ghe ghe = ve.getGhe(); if (ghe != null) { if (ghe.getMaGhe() != null && !ghe.getMaGhe().isEmpty()) seatInfo = ghe.getMaGhe(); else if (ghe.getViTriGhe() != null && !ghe.getViTriGhe().isEmpty()) seatInfo = ghe.getViTriGhe(); if (ghe.getToa() != null) { Object toaObj = ghe.getToa(); try { coachInfo = (String) toaObj.getClass().getMethod("getTenToa").invoke(toaObj); } catch (NoSuchMethodException nsme) { try { coachInfo = String.valueOf(toaObj.getClass().getMethod("getSoToa").invoke(toaObj)); } catch (Exception ignored) { } } catch (Exception ignored) { } } if (ve.getNgayDat() != null) departureTime = ve.getNgayDat(); } // Incorrect departure time source - kept as is per request
        else if (ve.getGhe() != null && ve.getGhe().getMaGhe() != null) { // Fallback using maGhe which comes from Ghe object according to previous code
            String maGhe = ve.getGhe().getMaGhe();
            if (maGhe.toUpperCase().startsWith("T")) { int sep = maGhe.indexOf('-'); if (sep > 1) coachInfo = maGhe.substring(1, sep); }
            int sep = maGhe.indexOf('-'); if (sep != -1 && sep < maGhe.length() - 1) seatInfo = maGhe.substring(sep + 1); else if (!maGhe.toUpperCase().startsWith("T")) seatInfo = maGhe;
        }
        } catch (Exception e) { System.err.println("Error accessing details via ve.getGhe() or parsing maGhe: " + e.getMessage()); if (ve.getGhe() != null && ve.getGhe().getMaGhe() != null) { seatInfo = "?"; coachInfo = "? (" + ve.getGhe().getMaGhe() + ")"; } } // Added null check for ve.getGhe()
        txtViTriGhe.setText(seatInfo); txtToa.setText(coachInfo); txtNgayKhoiHanhDisplay.setText(departureTime != null ? departureTime.format(DATETIME_FORMAT) : "");
    }


    // --- clearTicketDetails (Giữ nguyên code của bạn) ---
    private void clearTicketDetails() {
        // Your existing implementation...
        txtHoVaTenKH.clear(); txtSdtKH.clear(); txtCccdKH.clear(); txtEmailKH.clear(); txtLoaiVe.clear(); txtViTriGhe.clear(); txtGaKhoiHanh.clear(); txtNgayKhoiHanhDisplay.clear(); txtGiaVe.clear(); txtToa.clear(); txtGaDen.clear(); txtNgayDatDisplay.clear(); ticketTable.getSelectionModel().clearSelection();
    }

    // --- Event Handlers Setup ---
    private void setupListeners() {
        // <<< SỬA ĐỔI: Listener cho ô tìm kiếm gọi applySearchFilter >>>
        txtTimKiem.textProperty().addListener((obs, oldVal, newVal) -> applySearchFilter());
        txtTimKiem.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                applySearchFilter();
            }
        });

        // Listener chọn dòng (Giữ nguyên)
        ticketTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showTicketDetails(newSelection);
            } else {
                clearTicketDetails();
            }
        });

        // <<< SỬA ĐỔI: Listener cho DatePicker gọi applyDateFilter >>>
        if (dpDateField != null) {
            dpDateField.valueProperty().addListener((obs, oldDate, newDate) -> {
                System.out.println("DatePicker value changed to: " + newDate);
                applyDateFilter(); // Gọi hàm lọc theo ngày
            });
        }
    }

    // --- Button Actions Setup ---
    private void setupButtonActions() {
        // <<< SỬA ĐỔI: Nút Tìm kiếm gọi applySearchFilter >>>
        btnTimKiem.setOnAction(event -> applySearchFilter());

        // Các nút khác giữ nguyên
        btnInVe.setOnAction(event -> handleInVe());
        btnHuyVe.setOnAction(event -> handleHuyVe());
        btnCapNhat.setOnAction(event -> handleCapNhat());
    }

    // --- Filtering Logic ---

    // <<< BỔ SUNG: Hàm lọc CHỈ THEO NGÀY >>>
    private void applyDateFilter() {
        LocalDate selectedDate = (dpDateField != null) ? dpDateField.getValue() : null;

        filteredTicketList.setPredicate(ve -> {
            // Chỉ kiểm tra ngày
            return checkDate(ve, selectedDate);
        });

        ticketTable.refresh();
        clearTicketDetails();
    }
    // <<< KẾT THÚC BỔ SUNG >>>

    // <<< BỔ SUNG: Hàm lọc CHỈ THEO TÌM KIẾM (dùng toàn bộ masterList) >>>
    private void applySearchFilter() {
        String searchText = txtTimKiem.getText();

        // Luôn áp dụng predicate lên danh sách gốc masterTicketList thông qua filteredTicketList
        filteredTicketList.setPredicate(ve -> {
            // Chỉ kiểm tra text tìm kiếm
            return checkSearchText(ve, searchText);
        });

        ticketTable.refresh();
        clearTicketDetails();
    }
    // <<< KẾT THÚC BỔ SUNG >>>

    // --- Helper function for Search Text Check (Giữ nguyên code của bạn) ---
    private boolean checkSearchText(Ve ve, String searchText) {
        // Your existing implementation...
        if (searchText == null || searchText.isEmpty()) { return true; } if (ve == null) { return false; } String lowerCaseFilter = searchText.toLowerCase();
        if (ve.getMaVe() != null && ve.getMaVe().toLowerCase().contains(lowerCaseFilter)) { return true; }
        if (ve.getKhachHang() != null && ve.getKhachHang().getMaKhachHang() != null && ve.getKhachHang().getMaKhachHang().toLowerCase().contains(lowerCaseFilter)) { return true; } // Added null check
        if (ve.getGhe() != null && ve.getGhe().getMaGhe() != null && ve.getGhe().getMaGhe().toLowerCase().contains(lowerCaseFilter)) { return true; } // Added null check
        KhachHang kh = null; if (ve.getKhachHang() != null && ve.getKhachHang().getMaKhachHang() != null) { try { kh = khachHangDAO.findById(ve.getKhachHang().getMaKhachHang()); } catch (Exception e) { } } // Added null check
        if (kh != null) { if (kh.getTenKhachHang() != null && kh.getTenKhachHang().toLowerCase().contains(lowerCaseFilter)) { return true; } if (kh.getSoDienThoai() != null && kh.getSoDienThoai().toLowerCase().contains(lowerCaseFilter)) { return true; } if (kh.getCCCD() != null && kh.getCCCD().toLowerCase().contains(lowerCaseFilter)) { return true; } }
        NhaGa gaDi = null; try { gaDi = nhaGaDAO.findBySTTNhaGa(ve.getSttGaKhoiHnah()); } catch (Exception e) { } if (gaDi != null && gaDi.getTenNhaGa() != null && gaDi.getTenNhaGa().toLowerCase().contains(lowerCaseFilter)) { return true; }
        NhaGa gaDen = null; try { gaDen = nhaGaDAO.findBySTTNhaGa(ve.getSttGaDen()); } catch (Exception e) { } if (gaDen != null && gaDen.getTenNhaGa() != null && gaDen.getTenNhaGa().toLowerCase().contains(lowerCaseFilter)) { return true; }
        return false;
    }

    // --- Helper function for Date Check (Giữ nguyên code của bạn) ---
    private boolean checkDate(Ve ve, LocalDate selectedDate) {
        // Your existing implementation...
        if (selectedDate == null) { return true; } if (ve == null || ve.getNgayDat() == null) { return false; } return ve.getNgayDat().toLocalDate().equals(selectedDate);
    }


    // --- Action Handlers (Giữ nguyên code của bạn) ---
    private void handleXemChiTiet(Ve ve) {
        // Your existing implementation...
        if (ve != null) { System.out.println("Xem chi tiết button clicked for MaVe: " + ve.getMaVe()); showTicketDetails(ve); }
    }

    private void handleInVe() {
        // Your existing implementation...
        Ve selectedTicket = ticketTable.getSelectionModel().getSelectedItem(); if (selectedTicket != null) { System.out.println("Button 'In Vé' clicked for ticket: " + selectedTicket.getMaVe()); showAlert(Alert.AlertType.INFORMATION, "In vé", "Chức năng 'In Vé' chưa được triển khai.\nVé đã chọn: " + selectedTicket.getMaVe()); } else { showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng chọn một vé để in."); }
    }

    private void handleHuyVe() {
        // Your existing implementation...
        Ve selectedTicket = ticketTable.getSelectionModel().getSelectedItem(); if (selectedTicket != null) { if (selectedTicket.getTrangThaiVe() == TrangThaiVe.CHUAMUA) { showAlert(Alert.AlertType.INFORMATION, "Thông Báo", "Vé " + selectedTicket.getMaVe() + " đã được hủy trước đó."); return; } Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION); confirmation.setTitle("Xác nhận hủy vé"); confirmation.setHeaderText("Hủy vé " + selectedTicket.getMaVe() + "?"); confirmation.setContentText("Bạn có chắc chắn muốn hủy vé này?"); confirmation.showAndWait().ifPresent(response -> { if (response == ButtonType.OK) { System.out.println("Button 'Hủy vé' confirmed for ticket: " + selectedTicket.getMaVe()); try { selectedTicket.setTrangThaiVe(TrangThaiVe.CHUAMUA); selectedTicket.setNgayChinhSuaGanNhat(LocalDateTime.now()); boolean success = veDAO.update(selectedTicket); if (success) { showAlert(Alert.AlertType.INFORMATION, "Thành công", "Vé " + selectedTicket.getMaVe() + " đã được hủy."); masterTicketList.remove(selectedTicket); clearTicketDetails(); } else { selectedTicket.setTrangThaiVe(TrangThaiVe.DAMUA); showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể hủy vé " + selectedTicket.getMaVe() + " trong cơ sở dữ liệu."); } } catch (Exception e) { selectedTicket.setTrangThaiVe(TrangThaiVe.DAMUA); showAlert(Alert.AlertType.ERROR, "Lỗi Hủy Vé", "Đã xảy ra lỗi: " + e.getMessage()); e.printStackTrace(); } } }); } else { showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng chọn một vé để hủy."); }
    }

    private void handleCapNhat() {
        Ve selectedTicket = ticketTable.getSelectionModel().getSelectedItem();

        if (selectedTicket == null) {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng chọn một vé để cập nhật thông tin khách hàng.");
            return;
        }

        if (selectedTicket.getKhachHang() == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi Dữ Liệu", "Vé này không có thông tin khách hàng liên kết.");
            return;
        }

        try {
            String newName = txtHoVaTenKH.getText().trim();
            String newPhone = txtSdtKH.getText().trim();
            String newCccd = txtCccdKH.getText().trim();
            String newEmail = txtEmailKH.getText().trim();

            // Validation (giữ nguyên)
            if (newName.isEmpty()) {
                thongBao("Vui lòng nhập Họ và tên khách hàng.", txtHoVaTenKH);
                return;
            }
            if (newCccd.isEmpty()) {
                thongBao("Vui lòng nhập căn cước công dân khách hàng.", txtCccdKH);
                return;
            }
            if (!newCccd.matches("\\d{12}")) {
                thongBao("CCCD phải là 12 chữ số.", txtCccdKH);
                return;
            }
            if (newPhone.isEmpty()) {
                thongBao("Vui lòng nhập số điện thoại khách hàng.", txtSdtKH);
                return;
            }
            if (!newPhone.matches("\\d{10}")) {
                thongBao("Số điện thoại không hợp lệ (phải là 10 số).", txtSdtKH);
                return;
            }
            if (newEmail.isEmpty()) {
                thongBao("Vui lòng nhập email khách hàng.", txtEmailKH);
                return;
            }
            if (!newEmail.matches("^[a-zA-Z0-9._%+-]+@gmail.com$")) {
                thongBao("Địa chỉ email không hợp lệ.", txtEmailKH);
                return;
            }

            // Cập nhật đối tượng KhachHang
            KhachHang kh = selectedTicket.getKhachHang();
            kh.setTenKhachHang(newName);
            kh.setSoDienThoai(newPhone);
            kh.setCCCD(newCccd);
            kh.setEmail(newEmail);

            // Cập nhật thời gian chỉnh sửa và KhachHang của Vé
            selectedTicket.setNgayChinhSuaGanNhat(LocalDateTime.now());
            selectedTicket.setKhachHang(kh);

            // Cập nhật KhachHang trong cơ sở dữ liệu trước
            boolean khachHangUpdated = khachHangDAO.update(kh);
            if (!khachHangUpdated) {
                showAlert(Alert.AlertType.ERROR, "Lỗi Cập Nhật", "Không thể cập nhật thông tin khách hàng trong cơ sở dữ liệu.");
                return;
            }

            // Cập nhật Vé trong cơ sở dữ liệu
            boolean veUpdated = veDAO.update(selectedTicket);
            if (veUpdated) {
                // Cập nhật masterTicketList
                int index = masterTicketList.indexOf(selectedTicket);
                if (index >= 0) {
                    masterTicketList.set(index, selectedTicket);
                } else {
                    System.err.println("Selected ticket not found in masterTicketList.");
                }

                // Làm mới bảng
                ticketTable.refresh();

                // Chọn lại dòng và hiển thị chi tiết
                ticketTable.getSelectionModel().clearSelection();
                ticketTable.getSelectionModel().select(selectedTicket);

                // Đảm bảo hiển thị dữ liệu mới nhất
                showTicketDetails(selectedTicket);

                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thông tin vé đã được cập nhật.");

                // Áp dụng lại bộ lọc ngày nếu cần
                applyDateFilter();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi Cập Nhật", "Không thể cập nhật thông tin vé trong cơ sở dữ liệu.");
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật vé: " + (selectedTicket != null ? selectedTicket.getMaVe() : "null"));
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Cập Nhật", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }


    private void thongBao(String message, TextField txt) {
        showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", message);
        txt.requestFocus();
        txt.selectAll();
    }

    // --- Helper Methods (showAlert - Keep As Is) ---
    private void showAlert(Alert.AlertType type, String title, String message) {
        // ... Your existing showAlert code ...
        Alert alert = new Alert(type); alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(message); alert.showAndWait();
    }
}

