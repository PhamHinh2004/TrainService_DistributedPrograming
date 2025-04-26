package Enum;

public enum TrangThaiTau {
    DANGCHAY("Đang chạy"), DANGBAOTRI("Đang bảo trì"), NGUNGHOATDONG("Ngừng hoạt động");

    private String tenTrangThai;

    private TrangThaiTau(String tenTrangThai) {
        this.tenTrangThai = tenTrangThai;
    }

    @Override
    public String toString() {
        return tenTrangThai;
    }
}
