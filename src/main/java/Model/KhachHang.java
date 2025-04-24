package Model;

import jakarta.persistence.*;
import lombok.*;
import Enum.GioiTinh;
import org.checkerframework.checker.units.qual.N;

import java.util.List;
import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class KhachHang {
    @Id
    @EqualsAndHashCode.Include
    private String maKhachHang;

    private String tenKhachHang;
    private String soDienThoai;
    private String email;

    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    private String CCCD;

    // =====================
    @OneToMany(mappedBy = "khachHang")
    private Set<Ve> dsVe;

}
