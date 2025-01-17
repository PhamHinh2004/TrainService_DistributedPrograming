package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import Enum.TrangThaiHoaDon;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class HoaDon {
    @Id
    @EqualsAndHashCode.Include
    private String maHoaDon;

    private LocalDate ngayLapHoaDon;
    private int soLuongKhachHangNguoiLon;
    private int soLuongKhachHangTreEm;
    private String tenNguoiMua;
    private String soDienThoaiNguoiMua;
    private double thanhTien;
    private double tongTien;
    private LocalDate ngayChinhSuaGanNhat;

    @Enumerated(EnumType.STRING)
    private TrangThaiHoaDon trangThai;

    @OneToMany(mappedBy = "hoaDon")
    private List<ChiTietHoaDon> danhSachCTHD;

    public HoaDon() {}

    public HoaDon(String maHoaDon, LocalDate ngayLapHoaDon, int soLuongKhachHangNguoiLon, int soLuongKhachHangTreEm, String tenNguoiMua, String soDienThoaiNguoiMua, double thanhTien, double tongTien, LocalDate ngayChinhSuaGanNhat, TrangThaiHoaDon trangThai, List<ChiTietHoaDon> danhSachCTHD) {
        this.maHoaDon = maHoaDon;
        this.ngayLapHoaDon = ngayLapHoaDon;
        this.soLuongKhachHangNguoiLon = soLuongKhachHangNguoiLon;
        this.soLuongKhachHangTreEm = soLuongKhachHangTreEm;
        this.tenNguoiMua = tenNguoiMua;
        this.soDienThoaiNguoiMua = soDienThoaiNguoiMua;
        this.thanhTien = thanhTien;
        this.tongTien = tongTien;
        this.ngayChinhSuaGanNhat = ngayChinhSuaGanNhat;
        this.trangThai = trangThai;
        this.danhSachCTHD = danhSachCTHD;
    }
}
