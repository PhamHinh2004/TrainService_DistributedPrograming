package Enum;

public enum GioiTinh {
    Nam("Nam"), NU("Nữ");

    private String TenGioiTinh;

    private GioiTinh(String TenGioiTinh) {
        this.TenGioiTinh = TenGioiTinh;
    }

    @Override
    public String toString() {
        return TenGioiTinh;
    }
}
