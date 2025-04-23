package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import Enum.TrangThaiTaiKhoan;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
    @JoinColumn(name = "nhanvien", referencedColumnName = "nhanvien", nullable = false)
    private NhanVien nhanvien;

    @OneToMany(mappedBy = "taikhoan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NhomQuyen> nhomQuyen;
}