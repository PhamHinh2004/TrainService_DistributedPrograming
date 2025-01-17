package Model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import Enum.GioiTinh;

import java.util.List;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class KhachHang {
    @Id
    @EqualsAndHashCode.Include
    private String maKhachHang;
    private String tenKhachHang;
    private String soDienThoai;
    private String email;

    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    private String CCCD;

    @OneToMany(mappedBy = "khachHang")
    private List<ChiTietHoaDon> danhSachCTHD;

    public KhachHang() {}

    public KhachHang(String maKhachHang, String tenKhachHang, String soDienThoai, String email, GioiTinh gioiTinh, String CCCD, List<ChiTietHoaDon> danhSachCTHD) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.gioiTinh = gioiTinh;
        this.CCCD = CCCD;
        this.danhSachCTHD = danhSachCTHD;
    }
}
