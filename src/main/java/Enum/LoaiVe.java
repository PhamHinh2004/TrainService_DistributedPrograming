package Enum;

public enum LoaiVe {
    NGUOILON("Người lớn"),
    TREEM("Trẻ em");

    private String tenLoaiVe;

    LoaiVe(String tenLoaiVe) {
        this.tenLoaiVe = tenLoaiVe;
    }

    @Override
    public String toString() {
        return tenLoaiVe;
    }
}
