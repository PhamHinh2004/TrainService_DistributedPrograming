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

public class HoaDonExporter {

    public void generateInvoicePDF(HoaDon hoaDon) {
        String directory = "D:/HoaDon";
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = directory + "/HoaDon_" + hoaDon.getMaHoaDon() + ".pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            InputStream fontNormalStream = getClass().getResourceAsStream("/fonts/arial.ttf");
            InputStream fontBoldStream = getClass().getResourceAsStream("/fonts/arialbd.ttf");

            if (fontNormalStream == null || fontBoldStream == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Không tìm thấy file font Arial.");
                alert.showAndWait();
                return;
            }

            PDFont fontNormal = PDType0Font.load(document, fontNormalStream);
            PDFont fontBold = PDType0Font.load(document, fontBoldStream);

            float fontSize = 12;
            float leading = 1.5f * fontSize;
            float margin = 50;
            float yPosition = page.getMediaBox().getHeight() - margin;
            float pageWidth = page.getMediaBox().getWidth();

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                TextLineDrawer textDrawer = new TextLineDrawer(contentStream, fontNormal, fontSize, margin, yPosition);

                // Header
                textDrawer.setFont(fontBold);
                textDrawer.drawTextLine("Ga Gò Vấp", TextLineDrawer.TextAlignment.LEFT);

                textDrawer.moveTo(pageWidth - margin - (fontNormal.getStringWidth("Giờ in :" + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))) / 1000 * fontSize), yPosition);
                textDrawer.setFont(fontNormal);
                textDrawer.drawTextLine("Giờ in :" + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), TextLineDrawer.TextAlignment.LEFT);

                yPosition -= leading * 1.5f;
                textDrawer.setFont(fontBold);
                textDrawer.moveTo(margin, yPosition);
                textDrawer.drawTextLine("Hóa đơn mua vé", TextLineDrawer.TextAlignment.LEFT);

                yPosition -= leading * 1.2f;
                textDrawer.setFont(fontNormal);
                textDrawer.moveTo(margin, yPosition);
                textDrawer.drawTextLine("Ngày: " + hoaDon.getNgayLapHoaDon().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), TextLineDrawer.TextAlignment.LEFT);

                yPosition -= leading * 1.5f;
                textDrawer.drawTextLine("Tên công ty:", TextLineDrawer.TextAlignment.LEFT);
                textDrawer.moveTo(margin + 150, yPosition);
                textDrawer.drawTextLine("Nhà Ga Gò Vấp", TextLineDrawer.TextAlignment.LEFT);

                yPosition -= leading;
                textDrawer.moveTo(margin, yPosition);
                textDrawer.drawTextLine("Mã số thuế:", TextLineDrawer.TextAlignment.LEFT);
                textDrawer.moveTo(margin + 150, yPosition);
                textDrawer.drawTextLine("015613813235", TextLineDrawer.TextAlignment.LEFT);

                yPosition -= leading;
                textDrawer.moveTo(margin, yPosition);
                textDrawer.drawTextLine("Địa chỉ:", TextLineDrawer.TextAlignment.LEFT);
                textDrawer.moveTo(margin + 150, yPosition);
                textDrawer.drawMultiLineText(
                        "123/45 Đường Thống Nhất\nPhường 16 Gò Vấp Thành Phố\nHồ Chí Minh",
                        150
                );
                yPosition = textDrawer.getCurrentY() - leading * 0.5f; // Cập nhật yPosition sau khi vẽ nhiều dòng

                textDrawer.moveTo(margin, yPosition);
                textDrawer.drawTextLine("Hotline:", TextLineDrawer.TextAlignment.LEFT);
                textDrawer.moveTo(margin + 150, yPosition);
                textDrawer.drawTextLine("03258942154", TextLineDrawer.TextAlignment.LEFT);

                yPosition -= leading;
                textDrawer.moveTo(margin, yPosition);
                textDrawer.drawTextLine("Website:", TextLineDrawer.TextAlignment.LEFT);
                textDrawer.moveTo(margin + 150, yPosition);
                textDrawer.drawTextLine("gagv@gmail.com", TextLineDrawer.TextAlignment.LEFT);

                yPosition -= leading * 1.5f;

                // Table Header (ĐÃ SỬA)
                float tableStartX = margin;
                float currentY = yPosition;
                // STT, Mã vé, Loại vé, Mã ghế, Ga đi, Ga đến, Giá vé (ĐÃ LOẠI BỎ TÊN KHÁCH HÀNG)
                float[] columnWidths = {30, 80, 70, 60, 70, 70, 70}; // ĐÃ LOẠI BỎ 120 CHO TÊN KHÁCH HÀNG
                String[] headers = {"STT", "Mã vé", "Loại vé", "Mã ghế", "Ga đi", "Ga đến", "Giá vé"}; // ĐÃ LOẠI BỎ "Tên khách hàng"
                drawTableHeader(contentStream, tableStartX, currentY, columnWidths, headers, fontBold);
                currentY -= leading;

                // Table Data (ĐÃ SỬA)
                List<Ve> dsVeList = new ArrayList<>(hoaDon.getDsVe());
                drawTableData(document, contentStream, tableStartX, currentY, columnWidths, dsVeList, fontNormal, hoaDon);

                // Total Amount
                float totalAmountX = pageWidth - margin - 120; // CÓ THỂ CẦN ĐIỀU CHỈNH VỊ TRÍ NẾU BẢNG HẸP HƠN
                float totalAmountY = textDrawer.getCurrentY() - leading * 1.5f;
                textDrawer.setFont(fontBold);
                textDrawer.moveTo(totalAmountX, totalAmountY);
                textDrawer.drawTextLine("Tổng tiền:", TextLineDrawer.TextAlignment.LEFT);
                textDrawer.moveTo(totalAmountX + 90, totalAmountY);
                textDrawer.drawTextLine(String.format("%,.0f", hoaDon.getTongTien()), TextLineDrawer.TextAlignment.LEFT);

                // Footer
                textDrawer.setFont(fontNormal);
                float footerY = margin * 1.5f;
                textDrawer.moveTo(margin, footerY + leading);
                textDrawer.drawTextLine("Nhân viên phụ trách:", TextLineDrawer.TextAlignment.LEFT);
                if (hoaDon.getNhanVien() != null) {
                    textDrawer.moveTo(margin + 150, footerY + leading);
                    textDrawer.drawTextLine(hoaDon.getNhanVien().getTenNhanVien(), TextLineDrawer.TextAlignment.LEFT);
                }

                textDrawer.moveTo(margin, footerY);
                textDrawer.drawTextLine("Mã tra cứu hóa đơn:", TextLineDrawer.TextAlignment.LEFT);
                textDrawer.moveTo(margin + 150, footerY);
                textDrawer.drawTextLine(hoaDon.getMaHoaDon(), TextLineDrawer.TextAlignment.LEFT);

            }

            document.save(filePath);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Xuất hóa đơn thành công! Đã lưu vào " + filePath);
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra khi xuất hóa đơn: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void drawTableHeader(PDPageContentStream contentStream, float startX, float startY, float[] columnWidths, String[] headers, PDFont font) throws IOException {
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.setLineWidth(1);
        contentStream.setFont(font, 10);
        float x = startX;
        float y = startY;
        float rowHeight = 20;
        float cellMargin = 5;

        for (int i = 0; i < headers.length; i++) {
            float width = columnWidths[i];
            contentStream.addRect(x, y, width, rowHeight);
            drawText(contentStream, headers[i], x + cellMargin, y + (rowHeight / 2) + 3, width, TextAlignment.CENTER, font);
            x += width;
        }
        contentStream.stroke();
    }

    private void drawTableData(PDDocument document, PDPageContentStream contentStream, float startX, float startY, float[] columnWidths, List<Ve> data, PDFont font, HoaDon hoaDon) throws IOException {
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.setLineWidth(0.5f);
        contentStream.setFont(font, 10);
        float y = startY;
        float rowHeight = 18;
        float cellMargin = 5;
        float x;
        int stt = 1;

        // Tính tổng chiều rộng của tất cả các cột (ĐÃ SỬA)
        float totalRowWidth = 0;
        for (float width : columnWidths) {
            totalRowWidth += width;
        }

        for (Ve ve : data) {
            x = startX;
            contentStream.addRect(startX, y, totalRowWidth, rowHeight); // Sử dụng totalRowWidth
            drawText(contentStream, String.valueOf(stt++), x + cellMargin, y + (rowHeight / 2) + 3, columnWidths[0], TextAlignment.CENTER, font);
            x += columnWidths[0];
            drawText(contentStream, ve.getMaVe(), x + cellMargin, y + (rowHeight / 2) + 3, columnWidths[1], TextAlignment.CENTER, font);
            x += columnWidths[1];
            drawText(contentStream, ve.getLoaiVe().toString(), x + cellMargin, y + (rowHeight / 2) + 3, columnWidths[2], TextAlignment.CENTER, font);
            x += columnWidths[2];

            // Vẽ Mã ghế
            if (ve.getGhe() != null) {
                drawText(contentStream, ve.getGhe().getMaGhe(), x + cellMargin, y + (rowHeight / 2) + 3, columnWidths[3], TextAlignment.CENTER, font);
            } else {
                drawText(contentStream, "", x + cellMargin, y + (rowHeight / 2) + 3, columnWidths[3], TextAlignment.CENTER, font);
            }
            x += columnWidths[3];
            drawText(contentStream, hoaDon.getGaKhoiHanh(), x + cellMargin, y + (rowHeight / 2) + 3, columnWidths[4], TextAlignment.CENTER, font);
            x += columnWidths[4];
            drawText(contentStream, hoaDon.getGaDen(), x + cellMargin, y + (rowHeight / 2) + 3, columnWidths[5], TextAlignment.CENTER, font);
            x += columnWidths[5];
            drawText(contentStream, String.format("%,.0f", ve.getGiaVe()), x + cellMargin, y + (rowHeight / 2) + 3, columnWidths[6], TextAlignment.RIGHT, font);

            y -= rowHeight;
        }
        contentStream.stroke();
    }

    // Thêm phương thức để vẽ text nhiều dòng
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
            this.leading = 1.5f * fontSize;
        }

        public void drawTextLine(String text, TextAlignment alignment) throws IOException {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            float textWidth = font.getStringWidth(text) / 1000 * fontSize;
            float xPosition = currentX;

            switch (alignment) {
                case CENTER:
                    xPosition -= textWidth / 2;
                    break;
                case RIGHT:
                    xPosition -= textWidth;
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

        public void drawMultiLineText(String text, float maxWidth) throws IOException {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            List<String> lines = new ArrayList<>();
            String[] words = text.split("\\s+");
            StringBuilder currentLine = new StringBuilder();
            for (String word : words) {
                float wordWidth = font.getStringWidth(word + " ") / 1000 * fontSize;
                float currentLineWidth = font.getStringWidth(currentLine.toString()) / 1000 * fontSize;
                if (currentLineWidth + wordWidth <= maxWidth) {currentLine.append(word).append(" ");
                } else {
                    lines.add(currentLine.toString().trim());
                    currentLine = new StringBuilder(word).append(" ");
                }
            }
            lines.add(currentLine.toString().trim());

            for (String line : lines) {
                contentStream.setTextMatrix(1, 0, 0, 1, currentX, currentY);
                contentStream.showText(line);
                currentY -= leading;
            }
            contentStream.endText();
        }

        public void newLine(float factor) {
            currentY -= factor;
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

        enum TextAlignment {
            LEFT, CENTER, RIGHT
        }
    }

    private void drawText(PDPageContentStream contentStream, String text, float x, float y, float cellWidth, TextAlignment alignment, PDFont font) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, 10);
        float textWidth = font.getStringWidth(text) / 1000 * 10;
        float xOffset = 0;

        switch (alignment) {
            case CENTER:
                xOffset = (cellWidth - textWidth) / 2;
                break;
            case RIGHT:
                xOffset = cellWidth - textWidth - 2 * 5;
                break;
            case LEFT:
            default:
                xOffset = 5;
                break;
        }

        contentStream.setTextMatrix(new Matrix(1, 0, 0, 1, x + xOffset, y));
        contentStream.showText(text);
        contentStream.endText();
    }

    enum TextAlignment {
        LEFT, CENTER, RIGHT
    }
}