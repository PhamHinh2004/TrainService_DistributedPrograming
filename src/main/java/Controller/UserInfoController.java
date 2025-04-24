package Controller;

import Model.NhanVien; // Import NhanVien model
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class UserInfoController implements Initializable {

    @FXML private Label lblName;
    @FXML private Label lblId;
    @FXML private Label lblRole;
    @FXML private Label lblEmail;
    @FXML private Label lblPhone;
    @FXML private Label lblGender;
    @FXML private Label lblCccd;
    // Add other labels for NhanVien fields as needed

    private NhanVien user; // To hold the user data

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic here (optional)
        // Data is set by the calling controller (MainController) via setUserInfo()
    }

    // Method to receive user data from the calling controller
    public void setUserInfo(NhanVien user) {
        this.user = user;
        if (user != null) {
            // Populate labels with user data
            lblName.setText(user.getTenNhanVien() != null ? user.getTenNhanVien() : "N/A");
            lblId.setText(user.getMaNhanVien() != null ? user.getMaNhanVien() : "N/A");
            lblRole.setText(user.getChucVu() != null ? user.getChucVu() : "N/A");
            lblEmail.setText(user.getEmail() != null ? user.getEmail() : "N/A");
            lblPhone.setText(user.getSoDienThoai() != null ? user.getSoDienThoai() : "N/A");
            lblGender.setText(user.getGioiTinh() != null ? user.getGioiTinh().toString() : "N/A"); // Convert enum to String
            lblCccd.setText(user.getCCCD() != null ? user.getCCCD() : "N/A");

            // Add more labels for other fields
        } else {
            // Clear labels if user is null (shouldn't happen if called correctly)
            lblName.setText("N/A");
            lblId.setText("N/A");
            lblRole.setText("N/A");
            lblEmail.setText("N/A");
            lblPhone.setText("N/A");
            lblGender.setText("N/A");
            lblCccd.setText("N/A");
        }
    }
}