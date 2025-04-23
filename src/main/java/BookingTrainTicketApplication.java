 // Hoặc package của bạn, cần import Controller.LoginController nếu nó ở package khác

import Controller.LoginController; // Import LoginController
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BookingTrainTicketApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Tải file FXML của trang đăng nhập ban đầu
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml")); // Đảm bảo đường dẫn đúng
        Parent root = loader.load();

        // Thiết lập cửa sổ ban đầu cho trang Đăng nhập
        primaryStage.setTitle("Hệ Thống Đặt Vé Ga Tàu - Đăng Nhập");
        primaryStage.setScene(new Scene(root, 800, 600)); // Kích thước ban đầu cho cửa sổ đăng nhập
        primaryStage.show();

        // Lấy controller để có thể đóng EntityManager khi đóng ứng dụng
        // (Lưu ý: Phần đóng DB hiện đang bị comment trong LoginController.close())
        LoginController controller = loader.getController();
        primaryStage.setOnCloseRequest(e -> controller.close());
    }

    public static void main(String[] args) {
        launch(args);
    }
}