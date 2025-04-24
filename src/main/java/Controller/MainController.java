package Controller;

import Model.NhanVien; // Import NhanVien model
// Import other necessary classes
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays; // Needed for setupHoverEffects

public class MainController implements Initializable {

    @FXML
    private BorderPane mainLayout;

    @FXML
    private StackPane contentArea; // Khu vực này sẽ hiển thị nội dung của các trang khác

    // User info button and labels
    @FXML
    private javafx.scene.control.Button btnUserInfo;
    @FXML
    private Label lblUserName; // fx:id="lblUserName"
    @FXML
    private Label lblUserId;  // fx:id="lblUserId"

    // Standard buttons
    @FXML private javafx.scene.control.Button btnDatVe;
    @FXML private javafx.scene.control.Button btnQuanLyVe;
    @FXML private javafx.scene.control.Button btnQuanLyHoaDon;
    @FXML private javafx.scene.control.Button btnQuanLyKhachHang;
    @FXML private javafx.scene.control.Button btnQuanLyNhanVien; // Manager only
    @FXML private javafx.scene.control.Button btnDangXuat;
    @FXML private javafx.scene.control.Button btnQuanLyCaTruc; // Manager only


    // MenuButton and MenuItems for ThongKe (Manager only)
    @FXML
    private MenuButton mbThongKe;
    @FXML
    private MenuItem miThongKeNhaGa;
    @FXML
    private MenuItem miThongKeNhanVien;


    // Map to map Buttons to FXML paths (excluding user info and logout)
    private final Map<javafx.scene.control.Button, String> fxmlButtonMap = new HashMap<>();
    // MenuItems are handled separately

    // Keep track of the logged-in user
    private NhanVien loggedInUser;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Map the standard buttons to their FXML paths
        fxmlButtonMap.put(btnDatVe, "/View/DatVeView.fxml");
        fxmlButtonMap.put(btnQuanLyVe, "/View/QuanLyVeView.fxml");
        fxmlButtonMap.put(btnQuanLyHoaDon, "/View/QuanLyHoaDonView.fxml");
        fxmlButtonMap.put(btnQuanLyKhachHang, "/View/QuanLyKhachHangView.fxml");
        // Manager-specific buttons will be added to the map if they exist in FXML
        // We check for null below before putting them in the map
        if (btnQuanLyNhanVien != null) fxmlButtonMap.put(btnQuanLyNhanVien, "/View/QuanLyNhanVienView.fxml");
        if (btnQuanLyCaTruc != null) fxmlButtonMap.put(btnQuanLyCaTruc, "/View/QuanLyCaTrucView.fxml");


        // Note: UI visibility and initial page loading will happen after user is set in setLoggedInUser()

        // Setup handlers for buttons and menu items
        setupHandlers();

        // Setup hover effects for all potentially visible buttons
        // Pass all button references, null ones will be ignored by the helper
        setupHoverEffects(btnDatVe, btnQuanLyVe, btnQuanLyHoaDon,
                btnQuanLyKhachHang, btnQuanLyNhanVien, btnQuanLyCaTruc,
                btnDangXuat, btnUserInfo); // Add btnUserInfo here
        // mbThongKe hover styling is usually handled by CSS or default JavaFX
    }

    // Called by LoginController after successful login
    public void setLoggedInUser(NhanVien user) {
        this.loggedInUser = user;
        System.out.println("MainController received user: " + (user != null ? user.getTenNhanVien() : "null") + " (" + (user != null ? user.getChucVu() : "null") + ")");

        // Update the user info display in the sidebar
        if (user != null) {
            // Assuming lblUserName and lblUserId fx:ids are added to MainView.fxml and are not null
            if (lblUserName != null) lblUserName.setText(user.getTenNhanVien() != null ? user.getTenNhanVien() : "N/A");
            if (lblUserId != null) lblUserId.setText(user.getMaNhanVien() != null ? user.getMaNhanVien() : "N/A");

            // --- SET ACTION FOR USER INFO BUTTON BASED ON ROLE ---
            if (btnUserInfo != null) {
                btnUserInfo.setOnAction(event -> {
                    if (this.loggedInUser != null) {
                        System.out.println("User info button clicked. User role: " + this.loggedInUser.getChucVu());
                        if ("Quản lý".equals(this.loggedInUser.getChucVu())) {
                            // Load UserInfoView and pass data for Managers
                            showUserInfoPage(this.loggedInUser); // Calling the method below
                        } else {
                            // Load HomePageView for other roles (e.g., "Nhân viên")
                            loadContent("/View/HomePageView.fxml");
                        }
                    } else {
                        // Fallback if loggedInUser is somehow null here
                        loadContent("/View/HomePageView.fxml"); // Load default page
                    }
                });
            }

        } else {
            // Handle case where user is null (should ideally not happen after successful login)
            if (lblUserName != null) lblUserName.setText("Unknown User");
            if (lblUserId != null) lblUserId.setText("");
            if (btnUserInfo != null) btnUserInfo.setOnAction(null); // Disable button
            System.err.println("Error: Logged in user is null in setLoggedInUser.");
        }

        // Adjust UI visibility based on role
        adjustUIVisibility();

        // --- Load the initial page based on role AFTER visibility is adjusted ---
        // This overrides the default loadContent in initialize if set there
        if (loggedInUser != null) {
            if ("Quản lý".equals(loggedInUser.getChucVu())) {
                // Managers might land on their info page or another default
                // Let's have managers land on their info page initially
                showUserInfoPage(loggedInUser);
            } else {
                // Other roles land on the homepage
                loadContent("/View/HomePageView.fxml");
            }
        } else {
            // Fallback if somehow user is null
            loadContent("/View/HomePageView.fxml"); // Load default if no user info
        }
    }

    private void adjustUIVisibility() {
        // Determine if the logged-in user is a Manager
        boolean isManager = loggedInUser != null && "Quản lý".equals(loggedInUser.getChucVu());
        System.out.println("Adjusting UI for role: " + (isManager ? "Manager" : "Employee/Other"));


        // --- Apply visibility for Manager-specific items ---
        // Use the specific helper for Buttons
        setButtonVisibility(isManager, btnQuanLyNhanVien, btnQuanLyCaTruc);
        // Use the specific helper for MenuButtons
        setButtonVisibility(isManager, mbThongKe);


        // --- Apply visibility for items visible to ALL roles (or non-managers) ---
        // These buttons are visible to both roles in this example
        setButtonVisibility(true, btnQuanLyKhachHang, btnDangXuat, btnUserInfo);

        // Assuming these are visible only to non-managers based on the structure above
        setButtonVisibility(!isManager, btnDatVe, btnQuanLyVe, btnQuanLyHoaDon);

        // Note: MenuItems inside mbThongKe will inherit visibility from mbThongKe
    }

    // Helper method to set visibility and managed property for Button... varargs
    private void setButtonVisibility(boolean visible, javafx.scene.control.Button... buttons) {
        for (javafx.scene.control.Button button : buttons) {
            if (button != null) { // Check if the FXML element was injected
                button.setVisible(visible);
                button.setManaged(visible); // Remove from layout if not visible
            } else {
                // Log a warning if an FXML button is missing unexpectedly
                // System.err.println("Warning: Attempted to set visibility on a null Button reference.");
            }
        }
    }

    // Helper method to set visibility and managed property for MenuButton... varargs
    // UNCOMMENT THIS METHOD
    private void setButtonVisibility(boolean visible, MenuButton... buttons) {
        for (MenuButton button : buttons) {
            if (button != null) { // Check if the FXML element was injected
                button.setVisible(visible);
                button.setManaged(visible); // Remove from layout if not visible
            } else {
                // Log a warning if an FXML MenuButton is missing unexpectedly
                // System.err.println("Warning: Attempted to set visibility on a null MenuButton reference.");
            }
        }
    }


    // Phương thức để tải nội dung từ file FXML và hiển thị trong contentArea
    private void loadContent(String fxmlPath) {
        System.out.println("Loading content: " + fxmlPath);
        try {
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("Không tìm thấy file FXML: " + fxmlPath);
                displayError("Không tìm thấy file: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent content = loader.load();

            // --- OPTIONAL: Pass loggedInUser to content controllers ---
            // If your loaded views need the logged-in user (e.g., for filtering data)
            // you can try to get their controller and pass the user.
            // This requires the controllers of the loaded FXMLs to have a method like setLoggedInUser(NhanVien user).
            Object controller = loader.getController();
            // Example check if controller implements a specific interface
            // if (controller instanceof UserAwareController) {
            //     ((UserAwareController) controller).setLoggedInUser(loggedInUser);
            // } else
            if (controller instanceof UserInfoController) {
                // If loading UserInfoView via loadContent (e.g., directly from a menu,
                // though our logic uses showUserInfoPage), pass the user.
                // Note: showUserInfoPage already handles this. This might be redundant
                // but safer if loadContent is used elsewhere to load user-aware views.
                ((UserInfoController) controller).setUserInfo(loggedInUser);
            }
            // --- END OPTIONAL ---


            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);

        } catch (IOException e) {
            e.printStackTrace();
            displayError("Lỗi khi tải trang: " + fxmlPath + "\nChi tiết: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            displayError("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    // Phương thức hiển thị lỗi trong content area nếu có vấn đề
    private void displayError(String message) {
        contentArea.getChildren().clear();
        Label errorLabel = new Label("Lỗi: " + message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        VBox errorBox = new VBox(errorLabel);
        errorBox.setStyle("-fx-alignment: center;");
        contentArea.getChildren().add(errorBox);
    }


    // Keep your setupHandlers method
    private void setupHandlers() {
        // Setup handlers for standard Buttons using the map
        // The map only contains buttons we expect to load FXML content
        fxmlButtonMap.forEach((button, fxmlPath) -> {
            if (button != null) {
                button.setOnAction(event -> loadContent(fxmlPath));
            }
        });

        // Setup handlers for MenuItems
        if (mbThongKe != null) { // Check if MenuButton exists in FXML
            if (miThongKeNhaGa != null) { // Check if MenuItem exists in FXML
                miThongKeNhaGa.setOnAction(event -> loadContent("/View/ThongKeDoanhThuNhaGaView.fxml"));
            }
            if (miThongKeNhanVien != null) { // Check if MenuItem exists in FXML
                miThongKeNhanVien.setOnAction(event -> loadContent("/View/ThongKeDoanhThuNhanVienView.fxml")); // Or your specific employee stats view
            }
        }

        // Setup handler for the Logout button
        if (btnDangXuat != null) { // Check if button exists
            btnDangXuat.setOnAction(event -> handleLogout());
        }

        // btnUserInfo handler is set in setLoggedInUser based on role
        // No need to set it here initially with a default page load action
    }

    // Modified setupHoverEffect to accept multiple buttons
    private void setupHoverEffects(javafx.scene.control.Button... buttons) {
        for (javafx.scene.control.Button button : buttons) {
            if (button != null) {
                button.setOnMouseEntered(e ->
                        button.setStyle("-fx-background-color: #0052a3;") // Màu khi hover
                );

                button.setOnMouseExited(e -> {
                    button.setStyle("-fx-background-color: transparent;");
                });
            }
        }
        // Need to handle MenuButton hover separately, likely via CSS
    }

    // Placeholder for logout logic
    private void handleLogout() {
        System.out.println("Đang đăng xuất...");
        // Clear logged in user data (important for security/state management)
        this.loggedInUser = null;
        // Implement logout logic (close current stage, open login stage)
        Stage currentStage = (Stage) mainLayout.getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
            Parent loginRoot = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(loginRoot));
            loginStage.setTitle("Đăng nhập");
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load and display the User Info page specifically for Managers
    private void showUserInfoPage(NhanVien user) {
        if (user == null) {
            System.err.println("Error: Cannot show user info page for null user.");
            displayError("Không thể hiển thị thông tin cá nhân (dữ liệu trống).");
            loadContent("/View/HomePageView.fxml"); // Fallback to homepage
            return;
        }

        System.out.println("Attempting to load user info page for: " + user.getTenNhanVien());
        try {
            URL fxmlUrl = getClass().getResource("/View/UserInfoView.fxml"); // Path to your new FXML
            if (fxmlUrl == null) {
                System.err.println("Không tìm thấy file FXML Thông tin cá nhân: /View/UserInfoView.fxml");
                displayError("Không tìm thấy trang thông tin cá nhân.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent userInfoPage = loader.load(); // Load the FXML

            // Get the controller AFTER loading
            UserInfoController userInfoController = loader.getController();

            // Pass the logged-in user data to the UserInfoController
            if (userInfoController != null) {
                userInfoController.setUserInfo(user); // Pass the NhanVien object
                System.out.println("UserInfoController.setUserInfo called successfully.");
            } else {
                System.err.println("Error: UserInfoController is null after loading FXML.");
                displayError("Lỗi tải trang thông tin cá nhân: Không tìm thấy Controller.");
                // Optionally show the loaded page even if controller is null, it just won't have data
                // contentArea.getChildren().clear();
                // contentArea.getChildren().add(userInfoPage);
                return; // Stop here if controller is null
            }

            // Display the User Info page in the content area
            contentArea.getChildren().clear();
            contentArea.getChildren().add(userInfoPage);
            System.out.println("UserInfoView loaded and displayed.");

        } catch (IOException e) {
            e.printStackTrace();
            displayError("Lỗi khi tải trang thông tin cá nhân: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            displayError("Đã xảy ra lỗi khi tải trang thông tin cá nhân: " + e.getMessage());
        }
    }
}