package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import Enum.GioiTinh;
import Enum.TrangThaiNhanVien;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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
    @OneToMany(mappedBy = "nhanVien")
    private Set<CaTruc> dsCaTruc;

    @OneToOne(mappedBy = "nhanVien")
    private TaiKhoan taikhoan;

    @OneToMany(mappedBy = "nhanVien")
    private Set<HoaDon> dsHoaDon;

}