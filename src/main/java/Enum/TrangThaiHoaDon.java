package Enum;

public enum TrangThaiHoaDon {
    SUDUNG("Sử dụng"), KHONGSUDUNG("Không sử dụng");

    private String tenTrangThai;

    private TrangThaiHoaDon(String tenTrangThai) {
        this.tenTrangThai = tenTrangThai;
    }

    @Override
    public String toString() {
        return tenTrangThai;
    }
}
