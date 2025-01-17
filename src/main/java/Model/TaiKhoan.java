package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "taikhoan")
@Getter
@Setter
public class TaiKhoan {
    @Id
    @Column(name = "taikhoan_id")
    @EqualsAndHashCode.Include
    private String maTaiKhoan;

    @Column(name = "tenNhanVien", length = 50)
    private String tenTaiKhoan;

    @Column(name = "matKhau", length = 50)
    private String matKhau;

    @Column(name = "vaiTro", length = 50)
    private String vaiTro;

    @OneToOne
    @JoinColumn(name = "nhanvien_id", referencedColumnName = "nhanvien_id", nullable = false)
    private NhanVien nhanvien;

    @OneToMany(mappedBy = "taikhoan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Trang> trang;
}