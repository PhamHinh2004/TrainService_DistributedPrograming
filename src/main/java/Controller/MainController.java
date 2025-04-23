package Controller; // Đảm bảo package này khớp với cấu trúc dự án của bạn

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Import FXMLLoader để tải FXML khác
import javafx.fxml.Initializable;
import javafx.scene.Parent; // Import Parent (kiểu trả về khi tải FXML)
import javafx.scene.Scene; // Cần cho việc đóng cửa sổ
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane; // contentArea là StackPane
import javafx.scene.layout.VBox; // Dùng để tạo nội dung tạm hoặc hiển thị lỗi
import javafx.stage.Stage; // Cần cho việc đóng cửa sổ

import java.io.IOException; // Import IOException để xử lý lỗi khi tải FXML
import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap; // Dùng Map để quản lý dễ hơn
import java.util.Map;   // Dùng Map để quản lý dễ hơn

public class MainController implements Initializable {

    @FXML
    private BorderPane mainLayout;

    @FXML
    private StackPane contentArea; // Khu vực này sẽ hiển thị nội dung của các trang khác

    @FXML
    private Button btnDatVe;

    @FXML
    private Button btnQuanLyVe;

    @FXML
    private Button btnQuanLyHoaDon;

    @FXML
    private Button btnQuanLyKhachHang;

    @FXML
    private Button btnQuanLyNhanVien;

    @FXML
    private Button btnThongKe;

    @FXML
    private Button btnDangXuat;

    // Map để ánh xạ nút với đường dẫn FXML tương ứng
    // Lưu ý: Đảm bảo đường dẫn này đúng với vị trí file FXML của bạn trong classpath
    // (Ví dụ: nếu file FXML nằm trong src/main/resources/View/ thì đường dẫn sẽ là "/View/...")
    private final Map<Button, String> fxmlMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Ánh xạ các nút với đường dẫn file FXML của trang tương ứng
        // Bạn cần tạo các file FXML này, ví dụ: HomeView.fxml, DatVeView.fxml, ...
        fxmlMap.put(btnDatVe, "/View/DatVeView.fxml");
        fxmlMap.put(btnQuanLyVe, "/View/QuanLyVeView.fxml");
        fxmlMap.put(btnQuanLyHoaDon, "/View/QuanLyHoaDonView.fxml");
        fxmlMap.put(btnQuanLyKhachHang, "/View/QuanLyKhachHangView.fxml");
        fxmlMap.put(btnQuanLyNhanVien, "/View/QuanLyNhanVienView.fxml");
        fxmlMap.put(btnThongKe, "/View/ThongKeView.fxml");
        // Nút đăng xuất có logic riêng, không tải FXML

        // Tải và hiển thị trang chủ ban đầu
        // Thay thế cho showHomePage() cũ
        loadContent("/View/HomePageView.fxml"); // Giả sử bạn có file HomeView.fxml

        // Thiết lập các sự kiện cho các nút
        setupButtonHandlers();

        // Thiết lập hiệu ứng hover cho các nút
        // Giữ nguyên phần này như yêu cầu của bạn
        setupHoverEffect(btnDatVe);
        setupHoverEffect(btnQuanLyVe);
        setupHoverEffect(btnQuanLyHoaDon);
        setupHoverEffect(btnQuanLyKhachHang);
        setupHoverEffect(btnQuanLyNhanVien);
        setupHoverEffect(btnThongKe);
        setupHoverEffect(btnDangXuat);
    }

    // Phương thức để tải nội dung từ file FXML và hiển thị trong contentArea
    private void loadContent(String fxmlPath) {
        try {
            // Lấy URL của file FXML từ classpath
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("Không tìm thấy file FXML: " + fxmlPath);
                // Hiển thị thông báo lỗi trên giao diện nếu không tìm thấy file
                displayError("Không tìm thấy file: " + fxmlPath);
                return;
            }

            // Tải nội dung FXML
            Parent content = FXMLLoader.load(fxmlUrl);

            // Xóa nội dung hiện tại trong contentArea
            contentArea.getChildren().clear();
            // Thêm nội dung mới được tải vào contentArea
            contentArea.getChildren().add(content);

        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi khi tải FXML (ví dụ: cú pháp FXML sai)
            // Hiển thị thông báo lỗi trên giao diện
            displayError("Lỗi khi tải trang: " + fxmlPath + "\nChi tiết: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            // Bắt các lỗi khác có thể xảy ra trong quá trình tải/hiển thị
            displayError("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    // Phương thức hiển thị lỗi trong content area nếu có vấn đề
    private void displayError(String message) {
        contentArea.getChildren().clear(); // Xóa nội dung cũ
        Label errorLabel = new Label("Lỗi: " + message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        VBox errorBox = new VBox(errorLabel);
        errorBox.setStyle("-fx-alignment: center;"); // Căn giữa thông báo lỗi
        contentArea.getChildren().add(errorBox);
    }


    private void setupButtonHandlers() {
        // Thiết lập sự kiện OnAction cho các nút sử dụng Map
        // Thay thế các lệnh btnX.setOnAction(e -> showPage("...")) cũ
        fxmlMap.forEach((button, fxmlPath) -> {
            button.setOnAction(event -> loadContent(fxmlPath));
        });

        // Thiết lập sự kiện cho nút Đăng xuất riêng
        btnDangXuat.setOnAction(event -> handleLogout());
    }

    // Giữ nguyên hiệu ứng hover như code cũ của bạn
    private void setupHoverEffect(Button button) {
        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: #0052a3;") // Màu khi hover
        );

        button.setOnMouseExited(e -> {
            // Trở lại màu nền trong suốt khi không hover
            // Nếu bạn muốn một nút nào đó giữ màu khi đang active, cần thêm logic kiểm tra tại đây
            button.setStyle("-fx-background-color: transparent;");
        });
    }

    // Placeholder cho logic đăng xuất
    // Thay thế System.out.println("Đăng xuất") cũ
    private void handleLogout() {
        System.out.println("Đang đăng xuất...");
        // Thực hiện logic đăng xuất tại đây, ví dụ: đóng cửa sổ hiện tại
        // Lấy Stage hiện tại từ một node bất kỳ (ví dụ: mainLayout)
        Stage currentStage = (Stage) mainLayout.getScene().getWindow();
        currentStage.close();

     //    Tùy chọn: Mở lại cửa sổ đăng nhập nếu có
         try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml")); // Giả sử có LoginView.fxml
             Parent loginRoot = loader.load();
             Stage loginStage = new Stage();
             loginStage.setScene(new Scene(loginRoot));
             loginStage.setTitle("Đăng nhập");
             loginStage.show();
         } catch (IOException e) {
             e.printStackTrace();
             // Xử lý lỗi nếu không mở được cửa sổ đăng nhập
         }
    }

    // Các phương thức showHomePage() và showPage(String title) cũ đã được loại bỏ
    // vì logic của chúng đã được thay thế bởi phương thức loadContent(String fxmlPath).
}