package Enum;

public enum LoaiDichVu {
    GHEMEM("Ghế mềm"), GIUONGNAM("Giường nằm");

    private String tenDichVu;

    private LoaiDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    @Override
    public String toString() {
        return tenDichVu;
    }
}
