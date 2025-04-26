package Controller;

import Model.NhanVien;
import Model.TaiKhoan;
import Model.NhomQuyen;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

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

    private EntityManagerFactory emf;
    private EntityManager em;

    private NhanVien loggedInUser;

    @FXML
    public void initialize() {
        try {
            emf = Persistence.createEntityManagerFactory("default");
            em = emf.createEntityManager();
            System.out.println("LoginController initialized (DB connection active)");
        } catch (Exception e) {
            System.err.println("Error initializing JPA: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("LoginController initialized");

        // Add event listener to password field
        passwordField.setOnKeyPressed(this::handlePasswordFieldKeyPressed);
    }

    @FXML
    void handleLoginAction(ActionEvent event) {
        performLogin();
    }

    @FXML
    void handleForgotPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ForgotPassword.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Quên mật khẩu");

            ForgotPasswordController forgotPasswordController = loader.getController();
            forgotPasswordController.setLoginController(this);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading ForgotPassword FXML: " + e.getMessage());
        }
    }

    public TaiKhoan findTaiKhoanByEmail(String email) {
        try {
            String jpql = "SELECT tk FROM TaiKhoan tk JOIN FETCH tk.nhanVien nv WHERE nv.email = :email";
            TypedQuery<TaiKhoan> query = em.createQuery(jpql, TaiKhoan.class);
            query.setParameter("email", email);

            System.out.println("Executing JPQL: " + jpql);
            System.out.println("Searching for email: " + email);

            TaiKhoan taiKhoan = query.getSingleResult();

            System.out.println("TaiKhoan found: " + taiKhoan);
            if (taiKhoan != null && taiKhoan.getNhanVien() != null) {
                System.out.println("NhanVien email: " + taiKhoan.getNhanVien().getEmail());
            }

            return taiKhoan;
        } catch (jakarta.persistence.NoResultException e) {
            System.out.println("No TaiKhoan found for email: " + email);
            return null;
        } catch (Exception e) {
            System.err.println("Error in findTaiKhoanByEmail: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void resetPassword(TaiKhoan taiKhoan, String newPassword) {
        try {
            em.getTransaction().begin();
            taiKhoan.setMatKhau(newPassword);
            em.merge(taiKhoan);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            System.err.println("Error resetting password: " + e.getMessage());
        }
    }

    private void loadMainScreen(ActionEvent event, NhanVien loggedInUser, NhomQuyen userRole) {
        try {
            Stage currentStage;
            if (event != null) {
                Node source = (Node) event.getSource();
                currentStage = (Stage) source.getScene().getWindow();
            } else {
                // If the event is null (e.g., from Enter key), get the stage from any node
                currentStage = (Stage) userField.getScene().getWindow();
            }

            URL fxmlUrl = getClass().getResource("/View/MainView.fxml");
            if (fxmlUrl == null) {
                System.err.println("Không tìm thấy file FXML Màn hình chính: /View/MainView.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent mainAppRoot = loader.load();

            MainController mainController = loader.getController();
            mainController.setLoggedInUser(loggedInUser, userRole);

            Scene mainAppScene = new Scene(mainAppRoot);
            URL cssUrl = getClass().getResource("/CSS/style.css");
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
        if (em != null && em.isOpen()) em.close();
        if (emf != null) emf.close();
        System.out.println("LoginController close() called.");
    }

    private void performLogin() {
        String username = userField.getText();
        String password = passwordField.getText();

        System.out.println("Attempting login for user: " + username);

        if (em != null && em.isOpen()) {
            try {
                String jpql = "SELECT tk FROM TaiKhoan tk " +
                        "JOIN FETCH tk.nhomQuyen " +
                        "WHERE tk.tenTaiKhoan = :username AND tk.matKhau = :password";
                TypedQuery<TaiKhoan> query = em.createQuery(jpql, TaiKhoan.class);
                query.setParameter("username", username);
                query.setParameter("password", password);

                TaiKhoan taiKhoan = query.getSingleResult();

                if (taiKhoan != null) {
                    loggedInUser = taiKhoan.getNhanVien();
                    NhomQuyen userRole = taiKhoan.getNhomQuyen();

                    if (loggedInUser != null) {
                        System.out.println("Login successful for user: " + loggedInUser.getTenNhanVien() +
                                ", Role: " + userRole.getTenNhomQuyen());
                        loadMainScreen(null, loggedInUser, userRole); // Passing null ActionEvent
                    } else {
                        System.out.println("Login successful, but no NhanVien associated.");
                    }
                } else {
                    System.out.println("Login failed: Invalid username or password.");
                    // TODO: Show error
                }

            } catch (jakarta.persistence.NoResultException e) {
                System.out.println("Login failed: Invalid username or password.");
                // TODO: Show error
            } catch (Exception e) {
                System.err.println("Error during login: " + e.getMessage());
                e.printStackTrace();
                // TODO: Show error
            }
        } else {
            System.err.println("Entity Manager is not initialized or closed.");
            // TODO: Show error
        }
    }

    private void handlePasswordFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            performLogin();
        }
    }
}