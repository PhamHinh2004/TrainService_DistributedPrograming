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
import org.mindrot.jbcrypt.BCrypt;

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
        passwordField.setOnKeyPressed(this::handlePasswordFieldKeyPressed);
    }

    @FXML
    void handleLoginAction(ActionEvent event) {
        performLogin(event);
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

    public TaiKhoan findTaiKhoanByEmailOrUsername(String identifier) {
        try {
            String jpql = "SELECT tk FROM TaiKhoan tk JOIN FETCH tk.nhanVien nv WHERE nv.email = :identifier OR tk.tenTaiKhoan = :identifier";
            TypedQuery<TaiKhoan> query = em.createQuery(jpql, TaiKhoan.class);
            query.setParameter("identifier", identifier);
            return query.getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public NhanVien findNhanVienByTenTaiKhoan(String tenTaiKhoan) {
        try {
            String jpql = "SELECT nv FROM NhanVien nv JOIN FETCH nv.taikhoan tk WHERE tk.tenTaiKhoan = :tenTaiKhoan";
            TypedQuery<NhanVien> query = em.createQuery(jpql, NhanVien.class);
            query.setParameter("tenTaiKhoan", tenTaiKhoan);
            return query.getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void resetPassword(TaiKhoan taiKhoan, String newPassword) {
        try {
            em.getTransaction().begin();
            String hashedPassword = hashPassword(newPassword);
            if (hashedPassword != null) {
                taiKhoan.setMatKhau(hashedPassword);
                em.merge(taiKhoan);
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    private void loadMainScreen(ActionEvent event, NhanVien loggedInUser, NhomQuyen userRole) {
        try {
            Stage currentStage;
            if (event != null) {
                Node source = (Node) event.getSource();
                currentStage = (Stage) source.getScene().getWindow();
            } else {
                currentStage = (Stage) userField.getScene().getWindow();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainView.fxml"));
            Parent mainAppRoot = loader.load();

            MainController mainController = loader.getController();
            mainController.setLoggedInUser(loggedInUser, userRole);

            Scene mainAppScene = new Scene(mainAppRoot);
            URL cssUrl = getClass().getResource("/CSS/style.css");
            if (cssUrl != null) {
                mainAppScene.getStylesheets().add(cssUrl.toExternalForm());
            }

            currentStage.setScene(mainAppScene);
            currentStage.setTitle("Hệ thống đặt vé ga tàu");
            currentStage.setMaximized(true);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải màn hình chính.");
        }
    }

    private void performLogin(ActionEvent event) {
        String username = userField.getText();
        String password = passwordField.getText();

        if (em != null && em.isOpen()) {
            try {
                String jpql = "SELECT tk FROM TaiKhoan tk JOIN FETCH tk.nhomQuyen WHERE tk.tenTaiKhoan = :username";
                TypedQuery<TaiKhoan> query = em.createQuery(jpql, TaiKhoan.class);
                query.setParameter("username", username);

                TaiKhoan taiKhoan = query.getSingleResult();

                if (taiKhoan != null) {
                    if (BCrypt.checkpw(password, taiKhoan.getMatKhau())) {
                        loggedInUser = taiKhoan.getNhanVien();
                        NhomQuyen userRole = taiKhoan.getNhomQuyen();

                        if (loggedInUser != null) {
                            loadMainScreen(event, loggedInUser, userRole);
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Lỗi", "Tài khoản không có nhân viên liên kết.");
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Đăng nhập thất bại", "Mật khẩu không đúng!");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Đăng nhập thất bại", "Tài khoản không tồn tại!");
                }

            } catch (jakarta.persistence.NoResultException e) {
                showAlert(Alert.AlertType.ERROR, "Đăng nhập thất bại", "Tài khoản không tồn tại!");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Lỗi hệ thống", "Đã xảy ra lỗi trong quá trình đăng nhập.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kết nối đến hệ thống.");
        }
    }

    private void handlePasswordFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            performLogin(null);
        }
    }

    private String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            return null;
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void close() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null) emf.close();
        System.out.println("LoginController close() called.");
    }
}