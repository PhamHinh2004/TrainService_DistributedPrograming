package Controller;

import Model.NhanVien; // Import NhanVien model (assuming it's in the Model package)
import Enum.GioiTinh; // Needed if creating mock NhanVien
import Enum.TrangThaiNhanVien; // Needed if creating mock NhanVien
import jakarta.persistence.EntityManager; // Keep JPA imports for real login
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
import java.net.URL;
import java.time.LocalDate; // Needed for mock NhanVien
import java.util.HashSet; // Needed for mock NhanVien


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

    // Placeholder for mock user roles for testing without DB
    private NhanVien mockManagerUser;
    private NhanVien mockEmployeeUser;


    @FXML
    public void initialize() {
        // Tạm thời comment lại phần khởi tạo JPA để tập trung làm UI trước
        // Khi làm thật, bỏ comment và đảm bảo kết nối DB hoạt động
        // try {
        //     emf = Persistence.createEntityManagerFactory("default");
        //     em = emf.createEntityManager();
        //     System.out.println("LoginController initialized (DB connection active)");
        // } catch (Exception e) {
        //     System.err.println("Error initializing JPA: " + e.getMessage());
        //     e.printStackTrace();
        //     // Handle the error, maybe disable login button or show message
        // }

        // --- Mock Data for testing roles (REMOVE FOR REAL DB) ---
        mockManagerUser = new NhanVien("NV004", "Thế Khánh", "0561561546", "thekhanh@gmail.com", GioiTinh.NAM, "0452737778", LocalDate.of(1990, 1, 1), "Quản lý", TrangThaiNhanVien.DANGLAM, new HashSet<>(), null, new HashSet<>());
        mockEmployeeUser = new NhanVien("NV007", "Nguyễn Văn Test", "0912345678", "test.nv@example.com", GioiTinh.NAM, "789012345678", LocalDate.of(1995, 5, 5), "Nhân viên", TrangThaiNhanVien.DANGLAM, new HashSet<>(), null, new HashSet<>());
        // --- END Mock Data ---

        System.out.println("LoginController initialized");
    }

    @FXML
    void handleLoginAction(ActionEvent event) {
        String username = userField.getText();
        String password = passwordField.getText();

        System.out.println("Attempting login for user: " + username);

        // --- Implement REAL login check here using your DB/Service ---
        // Replace the mock logic below with actual authentication
        NhanVien loggedInUser = null;

        // Example Mock Login Logic (REPLACE THIS)
        if ("khanh".equalsIgnoreCase(username) && "password".equals(password)) { // Example credentials
            loggedInUser = mockManagerUser; // Log in as Manager
            System.out.println("Mock login successful for Manager: " + loggedInUser.getTenNhanVien());
        } else if ("test".equalsIgnoreCase(username) && "password".equals(password)) { // Example employee credentials
            loggedInUser = mockEmployeeUser; // Log in as Employee
            System.out.println("Mock login successful for Employee: " + loggedInUser.getTenNhanVien());
        } else {
            System.out.println("Mock login failed: Invalid username or password.");
            // TODO: Show an error message to the user
            return; // Stop if login fails
        }
        // --- END Mock Login Logic ---


        // If login was successful (loggedInUser is not null)
        if (loggedInUser != null) {
            loadMainScreen(event, loggedInUser); // Pass the logged-in user
        } else {
            // This else block should ideally handle database login failures
            // Since the mock logic has its own return, this might only be reached
            // if the mock logic fails to set loggedInUser AND doesn't return.
            System.out.println("Login failed.");
            // TODO: Show a generic login failed message
        }
    }

    @FXML
    void handleForgotPassword(ActionEvent event) {
        System.out.println("Xử lý quên mật khẩu: Chuyển sang trang quên mật khẩu.");
        // Keep your existing forgot password logic
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

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải FXML Quên mật khẩu: " + e.getMessage());
        }
    }

    // Phương thức để tải màn hình chính, NHẬN THÔNG TIN NHÂN VIÊN
    private void loadMainScreen(ActionEvent event, NhanVien loggedInUser) {
        try {
            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();

            URL fxmlUrl = getClass().getResource("/View/MainView.fxml");
            if (fxmlUrl == null) {
                System.err.println("Không tìm thấy file FXML Màn hình chính: /View/MainView.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent mainAppRoot = loader.load();

            // --- GET THE CONTROLLER INSTANCE ---
            MainController mainController = loader.getController();

            // --- PASS THE LOGGED-IN USER TO THE MAIN CONTROLLER ---
            mainController.setLoggedInUser(loggedInUser); // Call the new method in MainController


            Scene mainAppScene = new Scene(mainAppRoot);
            // Load and apply the CSS file
            URL cssUrl = getClass().getResource("/CSS/style.css"); // Ensure this path is correct for your main CSS
            if (cssUrl == null) {
                System.err.println("CSS file not found: /CSS/style.css");
            } else {
                mainAppScene.getStylesheets().add(cssUrl.toExternalForm());
            }
            currentStage.setScene(mainAppScene);
            currentStage.setTitle("Hệ thống đặt vé ga tàu");

            currentStage.setMaximized(true);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải FXML Màn hình chính: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Đã xảy ra lỗi không xác định khi tải màn hình chính: " + e.getMessage());
        }
    }

    public void close() {
        // if (em != null) em.close();
        // if (emf != null) emf.close();
        System.out.println("LoginController close() called.");
    }
}