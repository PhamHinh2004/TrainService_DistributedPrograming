package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import Enum.LoaiVe;
import Enum.TrangThaiVe;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Ve {
    @Id
    @EqualsAndHashCode.Include
    private String maVe;

//    private String maKhachHang;
//    private String maHoaDon;

    @Enumerated(EnumType.STRING)
    private LoaiVe loaiVe;

    private double giaVe;
    private int sttGaKhoiHnah;
    private int sttGaDen;
    private LocalDateTime ngayDat;
    private LocalDateTime ngayChinhSuaGanNhat;

    @Enumerated(EnumType.STRING)
    private TrangThaiVe trangThaiVe;

    // =================================
    @ManyToOne
    @JoinColumn(name = "maHoaDon")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maGhe")
    private Ghe ghe;
}
