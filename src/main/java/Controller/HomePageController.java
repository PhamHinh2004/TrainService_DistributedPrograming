package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    @FXML
    private Pane headerImagePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Đây là nơi bạn có thể thiết lập hình ảnh tiêu đề sau khi bạn thêm đường dẫn
        setupHeaderImage();
    }

    private void setupHeaderImage() {
        // Bạn sẽ thay đổi đường dẫn hình ảnh tại đây
        String imagePath = "/images/train_header.jpg"; // Đường dẫn đến hình ảnh tàu hỏa trong thư mục resources

        try {
            // Tạo ImageView với hình ảnh từ đường dẫn
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);

            // Thiết lập kích thước của ImageView
            imageView.setFitWidth(headerImagePane.getPrefWidth());
            imageView.setFitHeight(headerImagePane.getPrefHeight());
            imageView.setPreserveRatio(false);

            // Thêm ImageView vào pane
            headerImagePane.getChildren().add(0, imageView); // Thêm vào vị trí đầu tiên để nó nằm dưới text

        } catch (Exception e) {
            System.out.println("Không thể tải hình ảnh: " + e.getMessage());
        }

        // Cập nhật ngày giờ hiện tại (bạn có thể cập nhật định kỳ nếu muốn)
        updateDateTime();
    }

    private void updateDateTime() {
        // Định dạng và hiển thị ngày giờ hiện tại
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Bạn có thể cập nhật các Label ngày và giờ nếu bạn đặt fx:id cho chúng
        // dateLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        // timeLabel.setText(currentTime.format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}