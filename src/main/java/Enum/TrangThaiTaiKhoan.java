package Enum;

public enum TrangThaiTaiKhoan {
    HOATDONG("Hoạt động"), KHONGHOATDONG("Không hoạt động");

    private String tenTrangThai;

    private TrangThaiTaiKhoan(String tenTrangThai) {
        this.tenTrangThai = tenTrangThai;
    }

    @Override
    public String toString() {
        return tenTrangThai;
    }
}
