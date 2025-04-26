package Enum;

public enum TrangThaiNhanVien {
    DANGLAM("Đang làm"), NGHILAM("Nghỉ làm");

    private String tenTrangThai;

    private TrangThaiNhanVien(String tenTrangThai) {
        this.tenTrangThai = tenTrangThai;
    }

    @Override
    public String toString() {
        return tenTrangThai;
    }
}
