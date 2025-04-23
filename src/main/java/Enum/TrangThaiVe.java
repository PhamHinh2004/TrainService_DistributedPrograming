package Enum;

public enum TrangThaiVe {
    DAMUA("Đã mua"), CHUAMUA("Chưa mua");

    private String tenTrangThai;

    private TrangThaiVe(String tenTrangThai) {
        this.tenTrangThai = tenTrangThai;
    }

    @Override
    public String toString() {
        return tenTrangThai;
    }
}
