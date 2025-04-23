package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NhomQuyen {
    @Id
    @EqualsAndHashCode.Include
    private String maNhomQuyen;

    private String tenNhomQuyen;
    private String moTa;

    // =========================================
    @ManyToOne
    @JoinColumn(name = "taikhoan", referencedColumnName = "taikhoan", nullable = false)
    private TaiKhoan taikhoan;

    @ManyToMany
    @JoinTable(
            name = "trang_quyen",
            joinColumns = @JoinColumn(name = "nhomquyen_id"),
            inverseJoinColumns = @JoinColumn(name = "nhomquyen_id"))
    private Set<Quyen> quyen;
}
