package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import Enum.GioiTinh;
import Enum.TrangThaiNhanVien;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class NhanVien {

    @Id
    @EqualsAndHashCode.Include
    private String maNhanVien;

    private String tenNhanVien;
    private String soDienThoai;
    private String email;

    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;
    private String CCCD;
    private LocalDate ngaySinh;
    private String chucVu;

    @Enumerated(EnumType.STRING)
    private TrangThaiNhanVien trangThai;


    // ======================================
    @OneToMany(mappedBy = "nhanvien", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CaTruc> catruc;

    @OneToOne(mappedBy = "nhanvien", cascade = CascadeType.ALL)
    private TaiKhoan taikhoan;

    @OneToMany(mappedBy = "nhanVien")
    private Set<HoaDon> hoaDons;

}