package Controller;

import Dao.NhanVienDAO;
import Dao.TaiKhoanDAO;
import Model.NhanVien;
import Model.TaiKhoan;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import java.net.URL;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import Enum.GioiTinh;
import Enum.TrangThaiNhanVien;
import Enum.TrangThaiTaiKhoan;
import org.mindrot.jbcrypt.BCrypt;

public class QuanLyNhanVienController implements Initializable {

    // --- FXML Components ---
    // Employee Details Section
    @FXML private TextField txtMaNhanVien;
    @FXML private TextField txtEmail;
    @FXML private TextField txtHoVaTen;
    @FXML private TextField txtSoDienThoai;
    @FXML private TextField txtViTri;
    @FXML private ComboBox<GioiTinh> cbGioiTinh;
    @FXML private TextField txtCccd;
    @FXML private ComboBox<TrangThaiNhanVien> cbTrangThaiEdit; // ComboBox sửa trạng thái NV
    @FXML private Button btnCapNhat;
    @FXML private Button btnLamMoi;
    @FXML private Button btnXoa;

    // Search Section
    @FXML private TextField txtTimKiem;

    // Create Account Section
    @FXML private TextField txtNewCccd;
    @FXML private PasswordField pfNewPassword;
    @FXML private PasswordField pfConfirmPassword;
    @FXML private TextField txtNewViTri; // Ô nhập vị trí mới
    @FXML private TextField txtNewHoVaTen;
    @FXML private TextField txtNewEmail;
    @FXML private TextField txtNewSoDienThoai;
    @FXML private ComboBox<GioiTinh> cbNewGioiTinh; // ComboBox chọn giới tính mới
    @FXML private Button btnTaoTaiKhoan;

    // Table Section
    @FXML private ComboBox<String> cbTrangThaiLoc; // ComboBox lọc theo trạng thái
    @FXML private TableView<NhanVien> employeeTable; // Bảng hiển thị nhân viên
    @FXML private TableColumn<NhanVien, Integer> colStt;
    @FXML private TableColumn<NhanVien, String> colMaNhanVien;
    @FXML private TableColumn<NhanVien, String> colHoVaTen;
    @FXML private TableColumn<NhanVien, String> colCccd;
    @FXML private TableColumn<NhanVien, String> colSoDienThoai;
    @FXML private TableColumn<NhanVien, String> colEmail;
    @FXML private TableColumn<NhanVien, GioiTinh> colGioiTinh;
    @FXML private TableColumn<NhanVien, TrangThaiNhanVien> colTrangThai; // Cột trạng thái NV
    @FXML private TableColumn<NhanVien, String> colViTri; // Cột vị trí

    // --- Data ---
    private ObservableList<NhanVien> masterEmployeeList = FXCollections.observableArrayList(); // Danh sách gốc chứa tất cả NV
    private FilteredList<NhanVien> filteredEmployeeList; // Danh sách đã lọc để hiển thị lên bảng

    // --- DAO ---
    private NhanVienDAO nhanVienDAO = new NhanVienDAO(NhanVien.class); // Đối tượng truy cập dữ liệu Nhân Viên
    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO(TaiKhoan.class); // Đối tượng truy cập dữ liệu Tài Khoản

    // --- Constants ---
    private static final String FILTER_DANG_LAM = "Đang làm";
    private static final String FILTER_NGHI_LAM = "Nghỉ làm";
    private static final String FILTER_TAT_CA = "Tất cả";

    private static final Pattern DIGITS_ONLY_PATTERN = Pattern.compile("\\d+");


    /**
     * Phương thức khởi tạo Controller, được gọi sau khi các thành phần FXML được load.
     * Dùng để cài đặt giá trị ban đầu, gán dữ liệu cho ComboBox, thiết lập bảng, tải dữ liệu,...
     * @param location URL của file FXML.
     * @param resources ResourceBundle (nếu có).
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Đổ dữ liệu vào các ComboBox
        cbGioiTinh.setItems(FXCollections.observableArrayList(GioiTinh.values()));
        cbNewGioiTinh.setItems(FXCollections.observableArrayList(GioiTinh.values()));
        cbTrangThaiEdit.setItems(FXCollections.observableArrayList(TrangThaiNhanVien.values()));

        // Đổ dữ liệu và đặt giá trị mặc định cho ComboBox lọc trạng thái
        ObservableList<String> trangThaiOptions = FXCollections.observableArrayList(
                FILTER_DANG_LAM, FILTER_NGHI_LAM, FILTER_TAT_CA
        );
        cbTrangThaiLoc.setItems(trangThaiOptions);
        cbTrangThaiLoc.setValue(FILTER_DANG_LAM);

        // Cài đặt các cột cho TableView
        setupTableColumns();

        // Tải dữ liệu nhân viên từ nguồn (CSDL)
        loadAllEmployeeDataFromSource();

        // Tạo FilteredList để lọc và hiển thị dữ liệu
        filteredEmployeeList = new FilteredList<>(masterEmployeeList, p -> true);
        employeeTable.setItems(filteredEmployeeList);

        // Cài đặt các Listener (lắng nghe sự kiện)
        setupListeners();
        // Gán hành động cho các Button
        setupButtonActions();

        // Xử lý sự kiện khi chọn một dòng trong bảng
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showEmployeeDetails(newSelection); // Hiển thị chi tiết NV được chọn
            } else {
                clearEmployeeDetails(); // Xóa trắng form chi tiết nếu không chọn dòng nào
            }
        });

        // Áp dụng bộ lọc ban đầu (mặc định là "Đang làm")
        applyFilters();

        // Xóa trắng các form khi khởi động
        clearEmployeeDetails();
        clearNewAccountForm();
    }

    /**
     * Cài đặt các cột cho TableView (employeeTable).
     * Liên kết các cột với thuộc tính tương ứng của đối tượng NhanVien.
     * Cài đặt cột STT tự động.
     */
    private void setupTableColumns() {
        colMaNhanVien.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        colHoVaTen.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
        colCccd.setCellValueFactory(new PropertyValueFactory<>("CCCD"));
        colSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        colViTri.setCellValueFactory(new PropertyValueFactory<>("chucVu"));

        // Cài đặt hiển thị và tính toán cho cột STT
        colStt.setCellValueFactory(cellData -> {
            if (cellData.getTableView() != null && cellData.getTableView().getItems() != null && cellData.getValue() != null) {
                int index = cellData.getTableView().getItems().indexOf(cellData.getValue());
                return new SimpleIntegerProperty(index >= 0 ? index + 1 : 0).asObject();
            } else {
                return new SimpleObjectProperty<>(0);
            }
        });
        colStt.setSortable(false); // Không cho phép sắp xếp cột STT
        colStt.setStyle("-fx-alignment: CENTER;"); // Căn giữa cột STT
    }

    /**
     * Tải toàn bộ danh sách nhân viên từ DAO vào danh sách `masterEmployeeList`.
     */
    private void loadAllEmployeeDataFromSource() {
        masterEmployeeList.clear(); // Xóa dữ liệu cũ
        try {
            List<NhanVien> employeesFromDb = nhanVienDAO.getAll(); // Lấy tất cả NV từ CSDL
            if (employeesFromDb != null && !employeesFromDb.isEmpty()) {
                masterEmployeeList.addAll(employeesFromDb); // Thêm vào danh sách gốc
                System.out.println("Đã tải " + masterEmployeeList.size() + " nhân viên (tất cả trạng thái) từ nguồn dữ liệu.");
            } else {
                System.out.println("Không có dữ liệu nhân viên nào được tải.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải dữ liệu Nhân viên từ DAO: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Dữ Liệu", "Không thể tải danh sách nhân viên.");
        }
    }

    // --- UI Update Methods ---
    /**
     * Hiển thị thông tin chi tiết của nhân viên được chọn lên các trường nhập liệu.
     * @param employee Đối tượng NhanVien được chọn.
     */
    private void showEmployeeDetails(NhanVien employee) {
        if (employee == null) {
            clearEmployeeDetails();
            return;
        }
        txtMaNhanVien.setText(employee.getMaNhanVien());
        txtHoVaTen.setText(employee.getTenNhanVien());
        txtCccd.setText(employee.getCCCD());
        txtSoDienThoai.setText(employee.getSoDienThoai());
        txtEmail.setText(employee.getEmail());
        cbGioiTinh.setValue(employee.getGioiTinh());
        txtViTri.setText(employee.getChucVu());
        cbTrangThaiEdit.setValue(employee.getTrangThai());
    }

    /**
     * Xóa trắng các trường thông tin chi tiết nhân viên.
     */
    private void clearEmployeeDetails() {
        txtMaNhanVien.setText("");
        txtHoVaTen.setText("");
        txtCccd.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        cbGioiTinh.setValue(null);
        txtViTri.setText("");
        cbTrangThaiEdit.setValue(null);
    }

    /**
     * Xóa trắng các trường trong form tạo tài khoản mới.
     */
    private void clearNewAccountForm() {
        txtNewCccd.setText("");
        pfNewPassword.setText("");
        pfConfirmPassword.setText("");
        txtNewViTri.setText("");
        txtNewHoVaTen.setText("");
        txtNewEmail.setText("");
        txtNewSoDienThoai.setText("");
        cbNewGioiTinh.setValue(null); // Bỏ chọn giới tính
    }

    // --- Event Handlers Setup ---
    /**
     * Cài đặt các listener cho các control (ô tìm kiếm, combobox lọc).
     * Khi giá trị thay đổi, gọi hàm applyFilters để cập nhật bảng.
     */
    private void setupListeners() {
        // Lắng nghe thay đổi text trong ô tìm kiếm
        txtTimKiem.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        // Lắng nghe sự kiện nhấn Enter trong ô tìm kiếm
        txtTimKiem.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                applyFilters();
            }
        });
        // Lắng nghe thay đổi giá trị trong ComboBox lọc trạng thái
        cbTrangThaiLoc.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    // --- Filtering Logic ---
    /**
     * Áp dụng bộ lọc tìm kiếm (theo text) và lọc trạng thái vào danh sách nhân viên hiển thị trên bảng.
     */
    private void applyFilters() {
        String filterText = (txtTimKiem.getText() == null) ? "" : txtTimKiem.getText().toLowerCase().trim();
        String selectedStatus = cbTrangThaiLoc.getValue();
        NhanVien selectedItem = employeeTable.getSelectionModel().getSelectedItem();

        // Đặt điều kiện lọc cho FilteredList
        filteredEmployeeList.setPredicate(nhanVien -> {
            if (nhanVien == null) return false;

            // Kiểm tra lọc theo trạng thái
            boolean statusMatch = false;
            if (selectedStatus == null || selectedStatus.equals(FILTER_TAT_CA)) { statusMatch = true; }
            else if (selectedStatus.equals(FILTER_DANG_LAM)) { statusMatch = (nhanVien.getTrangThai() == TrangThaiNhanVien.DANGLAM); }
            else if (selectedStatus.equals(FILTER_NGHI_LAM)) { statusMatch = (nhanVien.getTrangThai() == TrangThaiNhanVien.NGHILAM); }

            // Kiểm tra lọc theo từ khóa tìm kiếm (chỉ khi trạng thái khớp)
            boolean searchMatch = false;
            if (statusMatch) {
                if (filterText.isEmpty()) { searchMatch = true; }
                else {
                    // Kiểm tra khớp ở các trường mong muốn (không phân biệt hoa thường)
                    boolean maNvMatch = nhanVien.getMaNhanVien() != null && nhanVien.getMaNhanVien().toLowerCase().contains(filterText);
                    boolean tenNvMatch = nhanVien.getTenNhanVien() != null && nhanVien.getTenNhanVien().toLowerCase().contains(filterText);
                    boolean cccdMatch = nhanVien.getCCCD() != null && nhanVien.getCCCD().toLowerCase().contains(filterText);
                    boolean sdtMatch = nhanVien.getSoDienThoai() != null && nhanVien.getSoDienThoai().contains(filterText); // SDT không cần lowercase
                    boolean emailMatch = nhanVien.getEmail() != null && nhanVien.getEmail().toLowerCase().contains(filterText);
                    searchMatch = maNvMatch || tenNvMatch || cccdMatch || sdtMatch || emailMatch; // Chỉ cần 1 trường khớp
                }
            }
            return statusMatch && searchMatch; // Hiển thị nếu cả 2 bộ lọc đều khớp
        });

        // Khôi phục dòng đã chọn nếu nó vẫn còn trong danh sách lọc
        if (selectedItem != null && filteredEmployeeList.contains(selectedItem)) {
            employeeTable.getSelectionModel().select(selectedItem);
        } else {
            // Nếu dòng đã chọn bị lọc mất, xóa lựa chọn và form chi tiết
            if (employeeTable.getSelectionModel().getSelectedItem() != null) {
                employeeTable.getSelectionModel().clearSelection();
            }
            if (employeeTable.getSelectionModel().getSelectedItem() == null) {
                clearEmployeeDetails();
            }
        }
        employeeTable.refresh(); // Cập nhật lại bảng (quan trọng cho STT)
    }

    // --- Button Action Setup ---
    /**
     * Gán các hàm xử lý sự kiện (ví dụ: handleUpdateEmployee) cho các nút bấm.
     */
    private void setupButtonActions() {
        // Nút Làm mới
        btnLamMoi.setOnAction(event -> {
            System.out.println("Làm mới dữ liệu...");
            loadAllEmployeeDataFromSource(); // Tải lại toàn bộ dữ liệu
            txtTimKiem.clear(); // Xóa ô tìm kiếm
            cbTrangThaiLoc.setValue(FILTER_DANG_LAM); // Đặt lại bộ lọc trạng thái về mặc định
            clearEmployeeDetails(); // Xóa form chi tiết
            clearNewAccountForm(); // Xóa form tạo mới
            // applyFilters() sẽ tự động được gọi do thay đổi cbTrangThaiLoc
        });
        // Nút Cập nhật
        btnCapNhat.setOnAction(event -> handleUpdateEmployee());
        // Nút Tạo tài khoản
        btnTaoTaiKhoan.setOnAction(event -> handleCreateNewAccount());
        // Nút Xóa
        if (btnXoa != null) {
            btnXoa.setOnAction(event -> handleDeleteEmployee());
        } else {
            System.err.println("WARNING: btnXoa is null.");
        }
    }

    // --- Button Action Implementations ---
    /**
     * Xử lý sự kiện khi nhấn nút Cập nhật.
     * Lấy thông tin từ form, xác nhận, kiểm tra thay đổi và gọi DAO để cập nhật.
     */
    private void handleUpdateEmployee() {
        NhanVien selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            // Hiển thị hộp thoại xác nhận
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Xác Nhận Cập Nhật");
            confirmationAlert.setHeaderText("Bạn có chắc chắn muốn cập nhật thông tin nhân viên " + selectedEmployee.getMaNhanVien() + " ?");
            confirmationAlert.setContentText("Dữ liệu khi đã sửa sẽ không thể khôi phục như ban đầu!");
            ButtonType buttonTypeYes = new ButtonType("Cập nhật", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeNo = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmationAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == buttonTypeYes) {
                // Lấy dữ liệu mới từ các trường nhập
                String newHoVaTen = txtHoVaTen.getText().trim();
                String newCccd = txtCccd.getText().trim();
                String newSoDienThoai = txtSoDienThoai.getText().trim();
                String newEmail = txtEmail.getText().trim();
                GioiTinh newGioiTinh = cbGioiTinh.getValue();
                String newViTri = txtViTri.getText().trim();
                TrangThaiNhanVien newTrangThai = cbTrangThaiEdit.getValue();

                // Kiểm tra thiếu thông tin
                if (newHoVaTen.isEmpty() || newCccd.isEmpty() || newSoDienThoai.isEmpty() ||
                        newEmail.isEmpty() || newGioiTinh == null || newViTri.isEmpty() ||
                        newTrangThai == null) {
                    showAlert(Alert.AlertType.WARNING, "Thiếu Thông Tin", "Vui lòng điền đầy đủ thông tin cập nhật, bao gồm cả Trạng thái.");
                    return;
                }

                // 2. Kiểm tra định dạng CCCD (12 chữ số)
                if (newCccd.length() != 12 || !DIGITS_ONLY_PATTERN.matcher(newCccd).matches()) {
                    showAlert(Alert.AlertType.WARNING, "Định Dạng Sai", "CCCD phải là 12 chữ số.");
                    txtNewCccd.requestFocus();
                    return;
                }
                // 3. Kiểm tra định dạng SĐT (10 chữ số)
                if (newSoDienThoai.length() != 10 || !DIGITS_ONLY_PATTERN.matcher(newSoDienThoai).matches()) {
                    showAlert(Alert.AlertType.WARNING, "Định Dạng Sai", "Số điện thoại phải là 10 chữ số.");
                    txtNewSoDienThoai.requestFocus();
                    return;
                }
                // 4. Kiểm tra định dạng Email (@gmail.com)
                if (!newEmail.toLowerCase().endsWith("@gmail.com")) {
                    showAlert(Alert.AlertType.WARNING, "Định Dạng Sai", "Email phải kết thúc bằng @gmail.com.");
                    txtNewEmail.requestFocus();
                    return;
                }

                // Kiểm tra xem có thay đổi nào không
                boolean changed = !Objects.equals(selectedEmployee.getTenNhanVien(), newHoVaTen) ||
                        !Objects.equals(selectedEmployee.getCCCD(), newCccd) ||
                        !Objects.equals(selectedEmployee.getSoDienThoai(), newSoDienThoai) ||
                        !Objects.equals(selectedEmployee.getEmail(), newEmail) ||
                        !Objects.equals(selectedEmployee.getGioiTinh(), newGioiTinh) ||
                        !Objects.equals(selectedEmployee.getChucVu(), newViTri) ||
                        !Objects.equals(selectedEmployee.getTrangThai(), newTrangThai);

                if (!changed) {
                    showAlert(Alert.AlertType.INFORMATION, "Thông tin", "Không có thay đổi nào để cập nhật.");
                    return;
                }

                // Cập nhật đối tượng trong bộ nhớ
                selectedEmployee.setTenNhanVien(newHoVaTen);
                selectedEmployee.setCCCD(newCccd);
                selectedEmployee.setSoDienThoai(newSoDienThoai);
                selectedEmployee.setEmail(newEmail);
                selectedEmployee.setGioiTinh(newGioiTinh);
                selectedEmployee.setChucVu(newViTri);
                selectedEmployee.setTrangThai(newTrangThai);

                // Gọi DAO để cập nhật vào CSDL
                try {
                    boolean updated = nhanVienDAO.update(selectedEmployee);
                    if (updated) {
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thông tin nhân viên đã được cập nhật.");
                        applyFilters(); // Áp dụng lại bộ lọc để cập nhật view
//                        employeeTable.getSelectionModel().clearSelection();
                        clearEmployeeDetails();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Lỗi Cập Nhật", "Không thể cập nhật thông tin nhân viên. Vui lòng thử lại.");
                        // Load lại dữ liệu để đảm bảo UI khớp với DB nếu cập nhật thất bại
                        loadAllEmployeeDataFromSource();
                        applyFilters();
                    }
                } catch (Exception e) {
                    // Xử lý lỗi hệ thống
                    System.err.println("Lỗi khi cập nhật nhân viên qua DAO: " + e.getMessage());
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Đã xảy ra lỗi khi cập nhật dữ liệu.");
                    loadAllEmployeeDataFromSource();
                    applyFilters();
                }
            } else {
                // Người dùng hủy cập nhật
                System.out.println("Hủy thao tác cập nhật nhân viên: " + selectedEmployee.getMaNhanVien());
                showEmployeeDetails(selectedEmployee); // Hiển thị lại thông tin cũ
            }
        } else {
            // Chưa chọn nhân viên nào
            showAlert(Alert.AlertType.WARNING, "Chưa Chọn Nhân Viên", "Vui lòng chọn một nhân viên để cập nhật.");
        }
    }

    /**
     * Xử lý sự kiện khi nhấn nút Tạo tài khoản.
     * Lấy thông tin, kiểm tra tính hợp lệ, tạo mã NV, mã TK, tên TK, mã hóa mật khẩu,
     * tạo đối tượng NhanVien và TaiKhoan, sau đó gọi DAO để lưu cả hai.
     */
    private void handleCreateNewAccount() {
        // Lấy dữ liệu từ form tạo mới
        String cccd = txtNewCccd.getText().trim();
        String password = pfNewPassword.getText(); // Lấy mật khẩu gốc
        String confirmPassword = pfConfirmPassword.getText();
        String viTri = txtNewViTri.getText().trim();
        String hoVaTen = txtNewHoVaTen.getText().trim();
        String email = txtNewEmail.getText().trim();
        String soDienThoai = txtNewSoDienThoai.getText().trim();
        GioiTinh selectedGioiTinh = cbNewGioiTinh.getValue();

        // --- Kiểm tra dữ liệu đầu vào ---
        // 1. Kiểm tra rỗng (bao gồm cả giới tính)
        if (cccd.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                viTri.isEmpty() || hoVaTen.isEmpty() || email.isEmpty() ||
                soDienThoai.isEmpty() || selectedGioiTinh == null ) {
            showAlert(Alert.AlertType.WARNING, "Thiếu Thông Tin", "Vui lòng điền đầy đủ tất cả các trường bắt buộc, bao gồm cả Giới tính.");
            return;
        }
        // 2. Kiểm tra định dạng CCCD (12 chữ số)
        if (cccd.length() != 12 || !DIGITS_ONLY_PATTERN.matcher(cccd).matches()) {
            showAlert(Alert.AlertType.WARNING, "Định Dạng Sai", "CCCD phải là 12 chữ số.");
            txtNewCccd.requestFocus();
            return;
        }
        // 3. Kiểm tra định dạng SĐT (10 chữ số)
        if (soDienThoai.length() != 10 || !DIGITS_ONLY_PATTERN.matcher(soDienThoai).matches()) {
            showAlert(Alert.AlertType.WARNING, "Định Dạng Sai", "Số điện thoại phải là 10 chữ số.");
            txtNewSoDienThoai.requestFocus();
            return;
        }
        // 4. Kiểm tra định dạng Email (@gmail.com)
        if (!email.toLowerCase().endsWith("@gmail.com")) {
            showAlert(Alert.AlertType.WARNING, "Định Dạng Sai", "Email phải kết thúc bằng @gmail.com.");
            txtNewEmail.requestFocus();
            return;
        }

        // 5. Kiểm tra xác nhận mật khẩu
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Xác Nhận Mật Khẩu Sai", "Mật khẩu và xác nhận mật khẩu không khớp.");
            pfConfirmPassword.requestFocus();
            return;
        }
        // --- Kết thúc kiểm tra ---

        // --- Tạo các giá trị cần thiết ---
        // Mã hóa mật khẩu
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            return; // Dừng nếu mã hóa lỗi
        }
        // Tạo mã nhân viên mới (NVXXX)
        String newMaNV = generateSequentialId("NV");
        if (newMaNV == null) {
            return; // Dừng nếu tạo mã NV lỗi
        }
        // Tạo mã tài khoản mới (TKXXX)
        String newMaTK = generateSequentialId("TK");
        if (newMaTK == null) {
            return; // Dừng nếu tạo mã TK lỗi
        }
        // Tạo tên tài khoản từ tên (vd: Nguyễn Hùng -> hung)
        String usernameBase = extractLastNameForUsername(hoVaTen);

        // --- Tạo đối tượng NhanVien ---
        NhanVien newEmployee = new NhanVien();
        newEmployee.setMaNhanVien(newMaNV);
        newEmployee.setTenNhanVien(hoVaTen);
        newEmployee.setSoDienThoai(soDienThoai);
        newEmployee.setEmail(email);
        newEmployee.setGioiTinh(selectedGioiTinh);
        newEmployee.setCCCD(cccd);
        newEmployee.setNgaySinh(LocalDate.now()); // Nên dùng DatePicker
        newEmployee.setChucVu(viTri);
        newEmployee.setTrangThai(TrangThaiNhanVien.DANGLAM); // Mặc định đang làm

        // --- Tạo đối tượng TaiKhoan ---
        TaiKhoan newTaiKhoan = new TaiKhoan();
        newTaiKhoan.setMaTaiKhoan(newMaTK);
        newTaiKhoan.setTenTaiKhoan(usernameBase);
        newTaiKhoan.setMatKhau(hashedPassword);
        newTaiKhoan.setTrangThai(TrangThaiTaiKhoan.HOATDONG);
        newTaiKhoan.setNhanVien(newEmployee);


        try {

            // Tạm thời gọi save riêng lẻ (Cần đảm bảo transaction nếu dùng cách này trong thực tế)
            boolean savedNV = nhanVienDAO.save(newEmployee);
            boolean savedTK = false;
            if (savedNV) { // Chỉ lưu TK nếu lưu NV thành công
                savedTK = taiKhoanDAO.save(newTaiKhoan);
                if (!savedTK) {
                    System.err.println("Lỗi: Lưu Nhân viên thành công nhưng lưu Tài khoản thất bại cho NV: " + newMaNV);

                    showAlert(Alert.AlertType.ERROR, "Lỗi Tạo Mới", "Lỗi khi tạo thông tin đăng nhập cho nhân viên.");
                }
            }

            if (savedNV && savedTK) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tài khoản nhân viên mới đã được tạo thành công với mã: " + newMaNV);
                masterEmployeeList.add(newEmployee);
                applyFilters();
                clearNewAccountForm();

                // Tùy chọn: tự động chọn và cuộn tới nhân viên mới
                if (filteredEmployeeList.contains(newEmployee)) {
                    employeeTable.getSelectionModel().select(newEmployee);
                    employeeTable.scrollTo(newEmployee);
                }
            } else if (savedNV && !savedTK) {

            }
            else {
                showAlert(Alert.AlertType.ERROR, "Lỗi Tạo Mới", "Không thể lưu thông tin nhân viên vào CSDL.");
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi tạo nhân viên/tài khoản mới qua DAO: " + e.getMessage());
            e.printStackTrace();

            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate entry")) {
                showAlert(Alert.AlertType.ERROR, "Lỗi Trùng Lặp", "Mã nhân viên, CCCD, Email hoặc Tên tài khoản đã tồn tại.");
            } else if (e.getMessage() != null && e.getMessage().toLowerCase().contains("constraint violation")) {
                showAlert(Alert.AlertType.ERROR, "Lỗi Ràng Buộc", "Đã xảy ra lỗi ràng buộc dữ liệu.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Đã xảy ra lỗi khi tạo tài khoản.");
            }
        }
    }

    /**
     * Tạo mã tuần tự dựa trên số lượng nhân viên hiện có trong danh sách.
     * Định dạng: prefix + 3 chữ số (VD: NV001, TK004).
     * @param prefix Tiền tố cho mã (VD: "NV", "TK").
     * @return Chuỗi mã mới được tạo, hoặc null nếu có lỗi.
     */
    private String generateSequentialId(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            System.err.println("Lỗi tạo mã: Tiền tố không được rỗng.");
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Tiền tố mã không hợp lệ.");
            return null;
        }
        try {

            long currentCount = masterEmployeeList.size();
            long nextNumber = currentCount + 1;

            String formattedNumber = String.format("%03d", nextNumber);

            String newId = prefix + formattedNumber;

            System.out.println("Đã tạo mã mới (" + prefix + "): " + newId);
            return newId;

        } catch (Exception e) {
            System.err.println("Lỗi khi tạo mã mới với tiền tố '" + prefix + "': " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tạo mã mới (" + prefix + "). Lỗi: " + e.getMessage());
            return null;
        }
    }

    /**
     * Trích xuất từ cuối cùng từ họ tên (thường là tên riêng theo quy ước VN),
     * chuyển thành chữ thường và loại bỏ dấu tiếng Việt.
     * @param fullName Họ và tên đầy đủ.
     * @return Tên đã xử lý (chữ thường, không dấu), hoặc chuỗi rỗng nếu đầu vào không hợp lệ.
     */
    private String extractLastNameForUsername(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "";

        String trimmedName = fullName.trim();
        String[] nameParts = trimmedName.split("\\s+");
        if (nameParts.length == 0) return "";

        String lastNameWithDiacritics = nameParts[nameParts.length - 1];
        String lowerCaseLastName = lastNameWithDiacritics.toLowerCase();

        // Bỏ dấu
        String normalized = Normalizer.normalize(lowerCaseLastName, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutDiacritics = pattern.matcher(normalized).replaceAll("");
        String finalUsernameBase = withoutDiacritics.replaceAll("đ", "d"); // Xử lý chữ 'đ'

        return finalUsernameBase;
    }

    /**
     * Mã hóa mật khẩu dạng text sử dụng thuật toán BCrypt.
     * Tự động tạo salt ngẫu nhiên cho mỗi lần mã hóa.
     * @param plainPassword Mật khẩu dạng text cần mã hóa.
     * @return Chuỗi hash BCrypt (bao gồm cả salt), hoặc null nếu mã hóa thất bại.
     */
    private String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            System.err.println("Không thể mã hóa mật khẩu rỗng.");
            return null;
        }
        try {
            // Mã hóa mật khẩu với salt được tạo ngẫu nhiên
            return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        } catch (Exception e) {
            System.err.println("Lỗi mã hóa mật khẩu: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Xảy ra lỗi trong quá trình mã hóa mật khẩu.");
            return null;
        }
    }

    /**
     * Xử lý sự kiện khi nhấn nút Xóa (thực chất là cập nhật trạng thái thành 'Nghỉ làm').
     * Hiển thị xác nhận và gọi DAO để cập nhật trạng thái.
     */
    private void handleDeleteEmployee() {
        String maNhanVienToDelete = txtMaNhanVien.getText();
        if (maNhanVienToDelete == null || maNhanVienToDelete.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Chưa Chọn Nhân Viên", "Vui lòng chọn một nhân viên từ bảng để xóa.");
            return;
        }
        Optional<NhanVien> employeeOptional = masterEmployeeList.stream()
                .filter(nv -> maNhanVienToDelete.equals(nv.getMaNhanVien()))
                .findFirst();
        if (!employeeOptional.isPresent()) {
            showAlert(Alert.AlertType.ERROR, "Không Tìm Thấy", "Không tìm thấy nhân viên với mã: " + maNhanVienToDelete + " trong danh sách.");
            return;
        }
        NhanVien employeeToDelete = employeeOptional.get();

        // Hiển thị hộp thoại xác nhận
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Xác Nhận Xóa");
        confirmationAlert.setHeaderText("Xác nhận xóa nhân viên " + maNhanVienToDelete );
        confirmationAlert.setContentText("Hành động này sẽ cập nhật trạng thái và không thể hoàn tác trực tiếp.");
        ButtonType buttonTypeYes = new ButtonType("Chuyển thành 'Nghỉ làm'", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeNo = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeYes) {
            try {

                boolean deleted = nhanVienDAO.xoaNhanVien(maNhanVienToDelete); // Giả định hàm này cập nhật trạng thái NV

                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Thành Công", "Xóa thaành công nhân viên " + maNhanVienToDelete);

                    employeeToDelete.setTrangThai(TrangThaiNhanVien.NGHILAM);

                    if (employeeToDelete.equals(employeeTable.getSelectionModel().getSelectedItem())) {
                        cbTrangThaiEdit.setValue(TrangThaiNhanVien.NGHILAM);
                    }

                    applyFilters();

                    if (!filteredEmployeeList.contains(employeeToDelete)) {
                        if(employeeTable.getSelectionModel().getSelectedItem() == null){
                            clearEmployeeDetails();
                        }
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi Cập nhật Trạng thái", "Không thể cập nhật trạng thái nghỉ làm cho nhân viên.");
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi cập nhật trạng thái nghỉ làm qua DAO (Mã: " + maNhanVienToDelete + "): " + e.getMessage());
                e.printStackTrace();
                // Xử lý lỗi ràng buộc hoặc hệ thống
                if (e.getMessage() != null && e.getMessage().toLowerCase().contains("constraint violation")) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi Ràng Buộc", "Không thể cập nhật trạng thái nhân viên này vì còn dữ liệu liên quan.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Đã xảy ra lỗi khi thực hiện thao tác.");
                }
                // Tải lại dữ liệu để đảm bảo nhất quán
                loadAllEmployeeDataFromSource();
                applyFilters();
            }
        } else {
            System.out.println("Hủy thao tác cập nhật trạng thái nghỉ làm cho nhân viên: " + maNhanVienToDelete);
        }
    }


    /**
     * Hàm trợ giúp để hiển thị hộp thoại thông báo đơn giản.
     * @param type Loại thông báo (ERROR, WARNING, INFORMATION, CONFIRMATION).
     * @param title Tiêu đề của hộp thoại.
     * @param message Nội dung thông báo.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // Không hiển thị header phụ
        alert.setContentText(message);
        alert.showAndWait(); // Hiển thị và chờ người dùng đóng
    }
}