package Enum;

public enum LoaiHoaDon {
    NHOM("Nhóm"),
    DON("Đơn");

    private String tenLoaiHoaDon;

    LoaiHoaDon(String tenLoaiHoaDon) {
        this.tenLoaiHoaDon = tenLoaiHoaDon;
    }

    @Override
    public String toString() {
        return tenLoaiHoaDon;
    }
}
