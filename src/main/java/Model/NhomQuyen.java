package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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
   @OneToMany (mappedBy = "nhomQuyen")
    private Set<TaiKhoan> dsTaiKhoan;

    @ManyToMany
    @JoinTable(
        name = "NhomQuyen_Quyen",
        joinColumns = @JoinColumn(name = "maNhomQuyen"),
        inverseJoinColumns = @JoinColumn(name = "maQuyen")
    )
    private Set<Quyen> dsQuyen;
}
