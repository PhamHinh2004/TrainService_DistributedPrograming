package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import Enum.TrangThaiHoaDon;
import Enum.LoaiHoaDon;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {
    @Id
    @EqualsAndHashCode.Include
    private String maHoaDon;

    @Enumerated(EnumType.STRING)
    private LoaiHoaDon loaiHoaDon;

    private LocalDate ngayLapHoaDon;
    private String gaKhoiHanh;
    private String gaDen;
    private String tenNguoiMua;
    private String soDienThoaiNguoiMua;
    private double thanhTien;
    private double tongTien;
    private LocalDateTime ngayChinhSuaGanNhat;

    @Enumerated(EnumType.STRING)
    private TrangThaiHoaDon trangThai;


// =======================================
    @ManyToOne
    @JoinColumn(name = "nhanvien_id")
    private NhanVien nhanVien;

}
