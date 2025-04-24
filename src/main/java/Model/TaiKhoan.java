package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import Enum.TrangThaiTaiKhoan;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TaiKhoan {
    @Id
    @EqualsAndHashCode.Include
    private String maTaiKhoan;

    private String tenTaiKhoan;
    private String matKhau;

    @Enumerated(EnumType.STRING)
    private TrangThaiTaiKhoan trangThai;

    // ===============================
    @OneToOne
    @JoinColumn(name = "nhanVien")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "maNhomQuyen")
    private NhomQuyen nhomQuyen;
}