package Controller; // Adjust package name if necessary

import Model.TaiKhoan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordController {

    @FXML
    private TextField emailOrUsernameField;

    // Reference to the LoginController (to access DB methods)
    private LoginController loginController;

    // Setter method to inject the LoginController
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    @FXML
    private void handleResetPasswordAction(ActionEvent event) {
        String input = emailOrUsernameField.getText();
        if (input == null || input.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng nhập email.");
            return;
        }

        if (!isValidEmail(input)) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Email không hợp lệ.");
            return;
        }

        // ---  Password reset logic  ---
        if (loginController != null) {
            TaiKhoan taiKhoan = loginController.findTaiKhoanByEmail(input); // Assuming this method is added to LoginController

            if (taiKhoan != null) {
                String newPassword = "1111";
                loginController.resetPassword(taiKhoan, newPassword); // Assuming this method is added to LoginController
                //  Send email (PLACEHOLDER -  IMPLEMENT PROPERLY)
                System.out.println("Mật khẩu mới đã được đặt thành '" + newPassword + "'.  Email đã được gửi đến " + input);
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Mật khẩu mới đã được đặt thành '" + newPassword + "'. Vui lòng kiểm tra email của bạn.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tìm thấy tài khoản với email này.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kết nối với hệ thống. Vui lòng thử lại sau.");
        }
    }

    //  Basic email validation
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailRegex);
        return matcher.matches();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    private void handleBackToLoginAction(ActionEvent event) {
        try {
            // Get the current stage
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close(); // Close the current stage (optional, depends on your flow)

            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml")); // Adjust path
            Parent root = loader.load();

            // You could get the controller if needed:
            // LoginController loginController = loader.getController();

            // Create a new scene and show it
            Scene scene = new Scene(root);
            Stage loginStage = new Stage(); // Create a new stage for login
            loginStage.setScene(scene);
            loginStage.setTitle("Đăng nhập Hệ thống"); // Set title
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle error loading login page
            System.err.println("Error loading Login FXML: " + e.getMessage());
        }
    }
}