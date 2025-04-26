package Util;

import Model.HoaDon;
import Model.Ve;
import javafx.scene.control.Alert;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.util.Matrix;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HoaDonExporter {

    private static final float MARGIN = 50;
    private static final float FONT_SIZE = 11;
    private static final float LINE_HEIGHT = 1.4f * FONT_SIZE; // Slightly reduced line height
    private static final Logger LOGGER = Logger.getLogger(HoaDonExporter.class.getName());

    public void generateInvoicePDF(HoaDon hoaDon) {
        String directory = "D:/HoaDon";
        File dir = new File(directory);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                LOGGER.log(Level.SEVERE, "Failed to create directory: " + directory);
                showAlert("Lỗi", "Không thể tạo thư mục để lưu hóa đơn.", Alert.AlertType.ERROR);
                return;
            }
        }
        String filePath = directory + "/HoaDon_" + hoaDon.getMaHoaDon() + ".pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            InputStream fontNormalStream = getClass().getResourceAsStream("/fonts/arial.ttf");
            InputStream fontBoldStream = getClass().getResourceAsStream("/fonts/arialbd.ttf");

            if (fontNormalStream == null || fontBoldStream == null) {
                showAlert("Lỗi", "Không tìm thấy file font Arial.", Alert.AlertType.ERROR);
                return;
            }

            PDFont fontNormal = PDType0Font.load(document, fontNormalStream);
            PDFont fontBold = PDType0Font.load(document, fontBoldStream);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                TextLineDrawer textDrawer = new TextLineDrawer(contentStream, fontNormal, FONT_SIZE, MARGIN, page.getMediaBox().getHeight() - MARGIN);
                float pageWidth = page.getMediaBox().getWidth();

                // Header
                textDrawer.setFont(fontBold);
                textDrawer.drawTextLine("Ga Gò Vấp", TextLineDrawer.TextAlignment.LEFT);

                textDrawer.moveTo(pageWidth - MARGIN - textWidth(fontNormal, "Giờ in: " + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))), textDrawer.getCurrentY());
                textDrawer.setFont(fontNormal);
                textDrawer.drawTextLine("Giờ in: " + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), TextLineDrawer.TextAlignment.LEFT);

                textDrawer.newLine(1.5f);
                textDrawer.setFont(fontBold);
                textDrawer.drawTextLine("HÓA ĐƠN MUA VÉ", TextLineDrawer.TextAlignment.CENTER, pageWidth - 2 * MARGIN);

                textDrawer.newLine(1.2f);
                textDrawer.setFont(fontNormal);
                textDrawer.drawTextLine("Ngày: " + hoaDon.getNgayLapHoaDon().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), TextLineDrawer.TextAlignment.LEFT);
                textDrawer.newLine(1.5f);

                // Thông tin công ty
                float companyInfoX = MARGIN;
                float companyInfoWidth = 150;
                textDrawer.drawLabelValuePair(fontNormal, fontBold, "Tên công ty:", "Nhà Ga Gò Vấp", companyInfoX, textDrawer.getCurrentY(), companyInfoWidth);
                textDrawer.newLine();
                textDrawer.drawLabelValuePair(fontNormal, fontBold, "Mã số thuế:", "015613813235", companyInfoX, textDrawer.getCurrentY(), companyInfoWidth);
                textDrawer.newLine();
                textDrawer.drawLabelValuePair(fontNormal, fontBold, "Địa chỉ:", "123/45 Đường Thống Nhất", companyInfoX, textDrawer.getCurrentY(), companyInfoWidth);
                textDrawer.moveTo(companyInfoX + companyInfoWidth, textDrawer.getCurrentY() - LINE_HEIGHT);
                textDrawer.drawTextLine("Phường 16 Gò Vấp Thành Phố", TextLineDrawer.TextAlignment.LEFT);
                textDrawer.newLine();
                textDrawer.moveTo(companyInfoX + companyInfoWidth, textDrawer.getCurrentY() - LINE_HEIGHT);
                textDrawer.drawTextLine("Hồ Chí Minh", TextLineDrawer.TextAlignment.LEFT);
                textDrawer.newLine(0.5f);
                textDrawer.drawLabelValuePair(fontNormal, fontBold, "Hotline:", "03258942154", companyInfoX, textDrawer.getCurrentY(), companyInfoWidth);
                textDrawer.newLine();
                textDrawer.drawLabelValuePair(fontNormal, fontBold, "Website:", "gagv@gmail.com", companyInfoX, textDrawer.getCurrentY(), companyInfoWidth);
                textDrawer.newLine(1.5f);

                // Table Header
                float tableStartX = MARGIN;
                float currentY = textDrawer.getCurrentY();
                float[] columnWidths = {30, 80, 80, 70, 70, 70, 70}; // Adjusted column widths
                String[] headers = {"STT", "Mã vé", "Loại vé", "Mã ghế", "Ga đi", "Ga đến", "Giá vé"};
                drawTableHeader(contentStream, tableStartX, currentY, columnWidths, headers, fontBold);
                currentY -= LINE_HEIGHT;
                textDrawer.setCurrentY(currentY);

                // Table Data
                List<Ve> dsVeList = new ArrayList<>(hoaDon.getDsVe());
                drawTableData(document, contentStream, tableStartX, textDrawer.getCurrentY(), columnWidths, dsVeList, fontNormal, hoaDon);
                textDrawer.newLine(0.5f);

                // Total Amount
                float totalAmountX = pageWidth - MARGIN - 150;
                textDrawer.setFont(fontBold);
                textDrawer.moveTo(totalAmountX, textDrawer.getCurrentY() - LINE_HEIGHT * 1.5f);
                textDrawer.drawTextLine("Tổng tiền:", TextLineDrawer.TextAlignment.LEFT);
                textDrawer.setFont(fontNormal);
                textDrawer.moveTo(totalAmountX + 100, textDrawer.getCurrentY());
                textDrawer.drawTextLine(String.format("%,.0f VNĐ", hoaDon.getTongTien()), TextLineDrawer.TextAlignment.RIGHT);

                // Footer
                textDrawer.setFont(fontNormal);
                float footerY = MARGIN * 1.5f;
                textDrawer.moveTo(MARGIN, footerY + LINE_HEIGHT);
                textDrawer.drawLabelValuePair(fontNormal, fontBold, "Nhân viên phụ trách:", hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getTenNhanVien() : "", MARGIN, footerY + LINE_HEIGHT, 150);
                textDrawer.newLine();
                textDrawer.drawLabelValuePair(fontNormal, fontBold, "Mã tra cứu hóa đơn:", hoaDon.getMaHoaDon(), MARGIN, footerY, 150);


            }

            document.save(filePath);
            showAlert("Thông báo", "Xuất hóa đơn thành công! Đã lưu vào " + filePath, Alert.AlertType.INFORMATION);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error generating invoice", e);
            showAlert("Lỗi", "Có lỗi xảy ra khi xuất hóa đơn: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void drawTableHeader(PDPageContentStream contentStream, float startX, float startY, float[] columnWidths, String[] headers, PDFont font) throws IOException {
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.setLineWidth(0.8f);
        contentStream.setFont(font, FONT_SIZE);
        float x = startX;
        float rowHeight = 25;
        float cellMargin = 5;

        for (int i = 0; i < headers.length; i++) {
            float width = columnWidths[i];
            contentStream.addRect(x, startY, width, rowHeight);
            drawText(contentStream, headers[i], x + cellMargin, startY + (rowHeight / 2) - (FONT_SIZE / 2), width - 2 * cellMargin, TextAlignment.CENTER, font);
            x += width;
        }
        contentStream.stroke();
    }

    private void drawTableData(PDDocument document, PDPageContentStream contentStream, float startX, float startY, float[] columnWidths, List<Ve> data, PDFont font, HoaDon hoaDon) throws IOException {
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.setLineWidth(0.5f);
        contentStream.setFont(font, FONT_SIZE);
        float y = startY;
        float rowHeight = 22;
        float cellMargin = 5;
        float x;
        int stt = 1;

        for (Ve ve : data) {
            x = startX;
            contentStream.addRect(startX, y, sum(columnWidths), rowHeight);

            drawText(contentStream, String.valueOf(stt++), x + cellMargin, y + (rowHeight / 2) - (FONT_SIZE / 2), columnWidths[0] - 2 * cellMargin, TextAlignment.CENTER, font);
            x += columnWidths[0];
            drawText(contentStream, ve.getMaVe(), x + cellMargin, y + (rowHeight / 2) - (FONT_SIZE / 2), columnWidths[1] - 2 * cellMargin, TextAlignment.LEFT, font);
            x += columnWidths[1];
            drawText(contentStream, ve.getLoaiVe().toString(), x + cellMargin, y + (rowHeight / 2) - (FONT_SIZE / 2), columnWidths[2] - 2 * cellMargin, TextAlignment.LEFT, font);
            x += columnWidths[2];
            drawText(contentStream, ve.getGhe() != null ? ve.getGhe().getMaGhe() : "", x + cellMargin, y + (rowHeight / 2) - (FONT_SIZE / 2), columnWidths[3] - 2 * cellMargin, TextAlignment.CENTER, font);
            x += columnWidths[3];
            drawText(contentStream, hoaDon.getGaKhoiHanh(), x + cellMargin, y + (rowHeight / 2) - (FONT_SIZE / 2), columnWidths[4] - 2 * cellMargin, TextAlignment.LEFT, font);
            x += columnWidths[4];
            drawText(contentStream, hoaDon.getGaDen(), x + cellMargin, y + (rowHeight / 2) - (FONT_SIZE / 2), columnWidths[5] - 2 * cellMargin, TextAlignment.LEFT, font);
            x += columnWidths[5];
            drawText(contentStream, String.format("%,.0f", ve.getGiaVe()), x + cellMargin, y + (rowHeight / 2) - (FONT_SIZE / 2), columnWidths[6] - 2 * cellMargin, TextAlignment.RIGHT, font);

            y -= rowHeight;
        }
        contentStream.stroke();
    }

    private static class TextLineDrawer {
        private final PDPageContentStream contentStream;
        private PDFont font;
        private final float fontSize;
        private float currentX;
        private float currentY;
        private final float leading;

        public TextLineDrawer(PDPageContentStream contentStream, PDFont font, float fontSize, float startX, float startY) {
            this.contentStream = contentStream;
            this.font = font;
            this.fontSize = fontSize;
            this.currentX = startX;
            this.currentY = startY;
            this.leading = 1.4f * fontSize; // Adjusted leading
        }

        public void drawTextLine(String text, TextAlignment alignment) throws IOException {
            drawTextLine(text, alignment, 0);
        }

        public void drawTextLine(String text, TextAlignment alignment, float availableWidth) throws IOException {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            float textWidth = textWidth(font, text);
            float xPosition = currentX;

            switch (alignment) {
                case CENTER:
                    xPosition += (availableWidth - textWidth) / 2;
                    break;
                case RIGHT:
                    xPosition += (availableWidth - textWidth);
                    break;
                case LEFT:
                default:
                    break;
            }
            contentStream.setTextMatrix(1, 0, 0, 1, xPosition, currentY);
            contentStream.showText(text);
            contentStream.endText();
            currentY -= leading;
        }

        public void drawLabelValuePair(PDFont labelFont, PDFont valueFont, String label, String value, float x, float y, float labelWidth) throws IOException {
            contentStream.beginText();
            contentStream.setFont(labelFont, fontSize);
            contentStream.setTextMatrix(1, 0, 0, 1, x, y);
            contentStream.showText(label);
            float valueX = x + labelWidth + 10; // Adjust spacing
            contentStream.setFont(valueFont, fontSize);
            contentStream.setTextMatrix(1, 0, 0, 1, valueX, y);
            contentStream.showText(value);
            contentStream.endText();
        }

        public void newLine(float factor) {
            currentY -= factor * leading;
        }

        public void newLine() {
            currentY -= leading;
        }

        public void moveTo(float x, float y) {
            currentX = x;
            currentY = y;
        }

        public float getCurrentY() {
            return currentY;
        }

        public void setFont(PDFont font) {
            this.font = font;
        }

        public void setCurrentY(float y) {
            this.currentY = y;
        }

        enum TextAlignment {
            LEFT, CENTER, RIGHT
        }
    }

    private void drawText(PDPageContentStream contentStream, String text, float x, float y, float cellWidth, TextAlignment alignment, PDFont font) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, FONT_SIZE);
        float textWidth = textWidth(font, text);
        float xOffset = 0;
        float padding = 5;

        switch (alignment) {
            case CENTER:
                xOffset = (cellWidth - textWidth) / 2;
                break;
            case RIGHT:
                xOffset = cellWidth - textWidth - padding;
                break;
            case LEFT:
            default:
                xOffset = padding;
                break;
        }

        contentStream.setTextMatrix(new Matrix(1, 0, 0, 1, x + xOffset, y));
        contentStream.showText(text);
        contentStream.endText();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static float textWidth(PDFont font, String text) throws IOException {
        return font.getStringWidth(text) / 1000 * FONT_SIZE;
    }

    private float sum(float[] arr) {
        float sum = 0;
        for (float val : arr) {
            sum += val;
        }
        return sum;
    }

    enum TextAlignment {
        LEFT, CENTER, RIGHT
    }
}