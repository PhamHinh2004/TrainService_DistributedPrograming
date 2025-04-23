package Controller; // Adjust package name if necessary

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordController {

    @FXML
    private TextField emailOrUsernameField;

    @FXML
    private void handleResetPasswordAction(ActionEvent event) {
        String input = emailOrUsernameField.getText();
        if (input == null || input.trim().isEmpty()) {
            // Show an error message (e.g., using an Alert)
            System.out.println("Please enter your email or username.");
            return;
        }

        // --- Add your password reset logic here ---
        // This would typically involve:
        // 1. Validating the input format (is it an email or username?)
        // 2. Looking up the user in your database.
        // 3. Generating a reset token or temporary password.
        // 4. Sending an email to the user with instructions or the new password.
        // 5. Showing a success or error message to the user (e.g., "Instructions sent to your email.").
        // --- End of reset logic ---

        System.out.println("Password reset requested for: " + input);

        // Optionally, navigate back to login or show a success message
        // For now, let's just print
        // You might want to show an alert instead:
        // Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // alert.setTitle("Yêu cầu đặt lại mật khẩu");
        // alert.setHeaderText(null);
        // alert.setContentText("Nếu tài khoản tồn tại, hướng dẫn đặt lại mật khẩu đã được gửi đến email/tên đăng nhập của bạn.");
        // alert.showAndWait();
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