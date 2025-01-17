package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@Table(name = "trang")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Trang {
    @Id
    @Column(name = "trang_id")
    @EqualsAndHashCode.Include
    private String maSoTrang;

    @Column(name = "tenTrang", length = 100)
    private String tenTrang;

    @ManyToOne
    @JoinColumn(name = "taikhoan_id", referencedColumnName = "taikhoan_id", nullable = false)
    private TaiKhoan taikhoan;

    @OneToMany(mappedBy = "trang", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Quyen> quyen;
}
