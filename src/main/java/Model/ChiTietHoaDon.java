package Model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class ChiTietHoaDon {
    @Id
    @ManyToOne
    @JoinColumn(name = "maHoaDon")
    private HoaDon hoaDon;

    @Id
    @ManyToOne
    @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    @Id
    @OneToOne
    @JoinColumn(name= "maVe", unique = true)
    private Ve ve;

    private double giaVe;
    private LocalDate ngayTaoChiTietHoaDon;
    private LocalDate ngayChinhSuaGanNhat;
    private double thueVAT;

    public ChiTietHoaDon() {}

    public ChiTietHoaDon(HoaDon hoaDon, KhachHang khachHang, Ve ve, double giaVe, LocalDate ngayTaoChiTietHoaDon, LocalDate ngayChinhSuaGanNhat, double thueVAT) {
        this.hoaDon = hoaDon;
        this.khachHang = khachHang;
        this.ve = ve;
        this.giaVe = giaVe;
        this.ngayTaoChiTietHoaDon = ngayTaoChiTietHoaDon;
        this.ngayChinhSuaGanNhat = ngayChinhSuaGanNhat;
        this.thueVAT = thueVAT;
    }




}
