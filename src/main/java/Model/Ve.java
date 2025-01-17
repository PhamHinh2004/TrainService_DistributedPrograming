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
public class Ve {
    @Id
    @EqualsAndHashCode.Include
    private String maVe;

    private LocalDate ngayKhoiHanh;
    private LocalDate ngayDatVe;
    private String nhaGaKhoiHanh;
    private String nhaGaKetThuc;
    private String loaiVe;
    private double giaVe;
    private String loaiGhe;
    private String viTriGhe;
    private String tenToa;
    private String soHieuTau;
    private String moTa;

    @ManyToOne
    @JoinColumn(name = "ghe_id")
    private Ghe ghe;

    @OneToOne(mappedBy = "ve")
    private ChiTietHoaDon chiTietHoaDon;

    public Ve() {}

    public Ve(String maVe, LocalDate ngayKhoiHanh, LocalDate ngayDatVe, String nhaGaKhoiHanh, String nhaGaKetThuc, String loaiVe, double giaVe, String loaiGhe, String viTriGhe, String tenToa, String soHieuTau, String moTa, ChiTietHoaDon chiTietHoaDon) {
        this.maVe = maVe;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.ngayDatVe = ngayDatVe;
        this.nhaGaKhoiHanh = nhaGaKhoiHanh;
        this.nhaGaKetThuc = nhaGaKetThuc;
        this.loaiVe = loaiVe;
        this.giaVe = giaVe;
        this.loaiGhe = loaiGhe;
        this.viTriGhe = viTriGhe;
        this.tenToa = tenToa;
        this.soHieuTau = soHieuTau;
        this.moTa = moTa;
        this.chiTietHoaDon = chiTietHoaDon;
    }
}
