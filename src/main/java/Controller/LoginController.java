package Controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL; // Import URL

public class LoginController {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMe;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink forgotPassword;

    // Tạm thời comment lại các dòng liên quan đến JPA/Hibernate
    // private EntityManagerFactory emf;
    // private EntityManager em;

    @FXML
    public void initialize() {
        // Tạm thời comment lại phần khởi tạo JPA để tập trung làm UI trước
        // Khi làm thật, bỏ comment và đảm bảo kết nối DB hoạt động
        // emf = Persistence.createEntityManagerFactory("default");
        // em = emf.createEntityManager();
        System.out.println("LoginController initialized (DB connection temporarily disabled)");
    }

    @FXML
    void handleLoginAction(ActionEvent event) {
        String username = userField.getText();
        String password = passwordField.getText(); // getPassword() an toàn hơn, nhưng getText() cũng hoạt động cho demo này

        System.out.println("Attempting login for user: " + username);

        // Tạm thời gọi loadMainScreen() ngay lập tức để demo chuyển trang UI
        // Khi làm thật, bạn phải di chuyển lệnh này vào TRONG khối IF
        // sau khi kiểm tra username/password với database thành công.
        loadMainScreen(event);

        // Phần kiểm tra đăng nhập bằng DB hiện đang bị comment và bị bỏ qua
        // vì lệnh loadMainScreen() chạy trước và chuyển trang rồi.
        /*
        try {
            // Kiểm tra đăng nhập sử dụng JPA
            // ... (bỏ comment khi làm thật)
            // if (!query.getResultList().isEmpty()) {
                 System.out.println("Đăng nhập thành công!");
                 loadMainScreen(event);
            // } else {
            //     System.out.println("Sai tên đăng nhập hoặc mật khẩu!");
            // }
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
        */
    }

    @FXML
    void handleForgotPassword(ActionEvent event) {
        System.out.println("Xử lý quên mật khẩu: Chuyển sang trang quên mật khẩu.");

        try {
            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();

            URL fxmlUrl = getClass().getResource("/View/ForgotPassword.fxml");
            if (fxmlUrl == null) {
                System.err.println("Không tìm thấy file FXML Quên mật khẩu: /View/ForgotPassword.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent forgotPasswordRoot = loader.load();

            Scene forgotPasswordScene = new Scene(forgotPasswordRoot);

            currentStage.setScene(forgotPasswordScene);
            currentStage.setTitle("Quên mật khẩu");
            // currentStage.sizeToScene();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải FXML Quên mật khẩu: " + e.getMessage());
        }
    }

    // Phương thức để tải màn hình chính
    private void loadMainScreen(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();

            // Đảm bảo đường dẫn "/View/MainView.fxml" là chính xác
            URL fxmlUrl = getClass().getResource("/View/MainView.fxml");
            if (fxmlUrl == null) {
                System.err.println("Không tìm thấy file FXML Màn hình chính: /View/MainView.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent mainAppRoot = loader.load();

            Scene mainAppScene = new Scene(mainAppRoot);

            currentStage.setScene(mainAppScene);
            currentStage.setTitle("Hệ thống đặt vé ga tàu");

            // --- THÊM DÒNG NÀY ĐỂ CỬA SỔ MAINVIEW LUÔN FULL MÀN HÌNH (MAXIMIZED) ---
            currentStage.setMaximized(true);

            // Bạn cũng đã set setResizable(false) trong MainApp ở câu trả lời trước,
            // nên không cần set lại ở đây trừ khi bạn muốn logic khác biệt.
            // currentStage.setResizable(false);


            // Stage đã hiển thị, không cần gọi show()

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải FXML Màn hình chính: " + e.getMessage());
        } catch (Exception e) { // Bắt các lỗi khác có thể xảy ra (như lỗi trong initialize của MainController)
            e.printStackTrace();
            System.err.println("Đã xảy ra lỗi không xác định khi tải màn hình chính: " + e.getMessage());
        }
    }

    // Giữ lại phương thức close, nhưng nó sẽ không làm gì khi emf và em là null
    public void close() {
        // if (em != null) em.close();
        // if (emf != null) emf.close();
        System.out.println("LoginController close() called (DB connection temporarily disabled)");
    }
}