import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View/MainView.fxml"));

        primaryStage.setTitle("Ga Gò Vấp");

        // Create the scene with the root node
        // The initial scene size (900x600) is still set here, but will be overridden by maximize
        Scene scene = new Scene(root, 900, 600);

        // Set the scene onto the stage
        primaryStage.setScene(scene);

        // --- Add these two lines to make it maximized and non-resizable ---

        // Make the window start maximized
        primaryStage.setMaximized(true);

        // Prevent the user from resizing the window
        primaryStage.setResizable(false);

        // ---------------------------------------------------------------

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
