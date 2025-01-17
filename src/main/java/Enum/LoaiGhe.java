package Enum;

public enum LoaiGhe {
    GheMem("ghế mềm"),
    GiuongToa6("giường toa 6"),
    GiuongToa4("giường toa 4"),
    GiuongToa2VIP("giường toa 2 VIP"),;

    private String tenLoaiGhe;

    LoaiGhe(String tenLoaiGhe) {
        this.tenLoaiGhe = tenLoaiGhe;
    }
}
